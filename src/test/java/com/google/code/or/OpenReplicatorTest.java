package com.google.code.or;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.db.ConMariadb;
import com.google.code.db.ConRedis;
import com.google.code.db.Constant;
import com.google.code.db.MultiThreadExecQuery;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.mysql.jdbc.PreparedStatement;

public class OpenReplicatorTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenReplicatorTest.class);

	public static void main(String args[]) throws Exception {
	
		final OpenReplicator or = new OpenReplicator();
	    final ConRedis cr = new ConRedis();
	    final Map<Integer, String>DBMap = new HashMap<>();
	    final Map<String, String>TableMap = new HashMap<>();
		final Map<Integer, Integer>tmpColumn = new HashMap<>();
		final Queue<String> queue = new LinkedList<>();
	    or.setUser(Constant.MySQLUser);
		or.setPassword(Constant.MySQLPwd);
		or.setHost(Constant.MySQLhost);
		or.setPort(Constant.MySQLport);
		or.setServerId(Constant.Serverid);
		or.setBinlogPosition(Constant.BinlogPosition);
		or.setBinlogFileName(Constant.BinlogFileName);
		or.setBinlogEventListener(new BinlogEventListener() {
		    public void onEvents(BinlogEventV4 event){
		        String events = event.toString();
		    	String header = events.substring(0, events.indexOf('='));
		    	//System.out.println(events);
		    	//SQL appears in log
		    	
		    	switch (header) {
				case "QueryEventheader":
					int index = events.lastIndexOf("sql");
					String statement = events.substring(index + 4);
					while(!statement.contains("BEGIN")){
						queue.add(statement + Constant.Semicolon);
						break;
					}
					break;
                case "XidEventheader":
                	//TODO trans to MQ
                	try {
            			Connection con = DriverManager.getConnection("jdbc:mariadb://10.77.50.80:3307/test", "root", null);
            			con.setAutoCommit(false);
            			while(!queue.peek().contains("INSERT")) {
            				queue.poll();
            			}
            			int indexOfFirstpar = queue.peek().indexOf('(');
    		    		int indexOfINTO = queue.peek().indexOf("INTO ");
    		    		String tmpTableName = queue.peek().substring(indexOfINTO + 5, indexOfFirstpar);
            			int NumOfCol = Toolmethod.countComma(queue.peek())/2 + 1;
    		    		java.sql.PreparedStatement pstmt1 = con
    		    				.prepareStatement(Toolmethod.prepareINSERT(tmpTableName, NumOfCol));
            			            		    
            			while(!queue.isEmpty()) {
            		    		String tmpStatement = queue.peek();
            		    		int indexOfSecondpar = tmpStatement.lastIndexOf('(');
            		    		String [] tmpVal = queue.poll()
            		    				.substring(indexOfSecondpar + 1, tmpStatement.length() - 2).split("\\,");
            		    		for(int i=0; i<tmpVal.length; i++) {
            		    			if (tmpVal[i].contains("'")) {
            		    				pstmt1.setObject(i + 1, Toolmethod.eraseApos(tmpVal[i]));
            		    			}else {
										pstmt1.setObject(i + 1, tmpVal[i]);
									}   
            		    		}
            		    		pstmt1.addBatch();           		    	  
            		      }	
            		    pstmt1.executeBatch();
            	    	con.commit();
            	    	 System.out.println("Commit a TX at" + System.currentTimeMillis());
            		} catch (Exception e) {
            			// TODO: handle exception
            			e.printStackTrace();
            		}
                    break;
                case "TableMapEventheader":
                	/*match tableid and tablename*/
                	Pattern p_tableid = Pattern.compile(Constant.Tableid_reg);
                	Matcher m_tableid = p_tableid.matcher(events);
                	Pattern p_tablename = Pattern.compile(Constant.Tablename_reg);
                	Matcher m_tablename = p_tablename.matcher(events);
                	if(m_tableid.find() && m_tablename.find()) {
                		TableMap.put(m_tableid.group(), m_tablename.group());
                	}
                	/*match column type*/
                	int i = 1;
                	Pattern p_map = Pattern.compile(Constant.Coltype_reg);
                	Matcher m_map = p_map.matcher(events);
                	if(m_map.find()){
                		String types[] = m_map.group().split("\\,"); 
                	for(String type : types){
                	   //System.out.println(type.replace(" ", ""));
                	   tmpColumn.put(i, Integer.parseInt(type.replace(" ", ""))); 
                	   i ++;
                	   }
                	}
                	break;
                case "WriteRowsEventheader":              	
                	/*match column values*/
                	try {
                	Connection conn = ConMariadb.getMariaCon();

                	Pattern p_write = Pattern.compile(Constant.Colvalue_reg);
                	Matcher m_write = p_write.matcher(events);
                	Pattern p_count = Pattern.compile(Constant.Colcount_reg);
                	Matcher m_count = p_count.matcher(events);
                	while (m_count.find()) {
                       int NumofCol = Integer.parseInt(m_count.group());
                  	   java.sql.PreparedStatement pstmt = conn
                				.prepareStatement(Toolmethod.prepareINSERT(Toolmethod.getRes(Constant.Tableid_reg, events), NumofCol));    
					
                	while(m_write.find()){
                		String value[] = m_write.group().split("\\,");
                		for (int j=0; j<value.length-1; j++){   
                			pstmt.setObject(j+1, value[j].replace(" ", ""));
							}
                		pstmt.addBatch();
                		}     
                	  pstmt.executeBatch();
                	}
                	
                	tmpColumn.clear();
            		i = 1;
                	}catch (Exception e) {
						// TODO: handle exception
					}
                	
                	//SQL that was rebuild                	               	
                case "RotateEventheader":
                	//DO Nothing
                	break;
                case "FormatDescriptionEventheader":
                	//DO Nothing
                	break;
				default:
					break;
				}
		        	//LOGGER.info("{}", event);		        			        	
 		    	}			    	
		    
		});
		or.start();

		//
		LOGGER.info("press 'q' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(String line = br.readLine(); line != null; line = br.readLine()) {
		    if(line.equals("q")) {
		        or.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		        break;
		    }
		}
	}
}
