package com.google.code.db;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Queue;

public class MultiThreadExecQuery extends Thread {
	public Queue<String> queue = new LinkedList<>();
	public MultiThreadExecQuery(Queue<String> queue) {
		this.queue = queue;
	}
	public void run(){
		try {
			Connection con = ConMariadb.getMariaCon();
			con.setAutoCommit(false);
			java.sql.PreparedStatement pstmt1 = con.prepareStatement("");
		    while(!queue.isEmpty()) {		    	
		    	pstmt1.addBatch(queue.poll());
		    	System.out.println("into thread");
		    }	
		    pstmt1.executeBatch();
	    	con.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	//	System.out.println(NorSQL);		       
	}
}
