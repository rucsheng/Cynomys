package com.google.code.or;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class PreparedStatementTest {
		public static void main(String[] args) {
			
		
		//public static void getMariaCon() throws Exception{
			// TODO Auto-generated method stub
			//create connection for a server installed in localhost, with a user "root" with no password
			//Class.forName("org.mariadb.jdbc.Driver");
			//Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");
	   try {
			Connection conn = DriverManager.getConnection("jdbc:mariadb://10.77.50.80:3307/test", "root", null);
	        // create a Statement
	        //Statement stmt = conn.createStatement();
			String sql = "insert into employees values (?,?,?,?,?,?);";
			java.sql.PreparedStatement pstat = conn.prepareStatement(sql);
			//pstat.setInt(1, 1);
			pstat.setObject(1, "10");
			//pstat.setString(2, "1919-12-12");
			pstat.setObject(2, "1992-11-21");
			pstat.setString(3, "Alex");
			pstat.setString(4, "Lok");
			pstat.setString(5, "M");
			pstat.setString(6, "1944-11-12");
			pstat.addBatch();
			pstat.setObject(1, 4);
			//pstat.setString(2, "1919-12-12");
			pstat.setObject(2, "1992-11-21");
			pstat.setString(3, "Alex");
			pstat.setString(4, "Lok");
			pstat.setString(5, "M");
			pstat.setString(6, "1944-11-12");
			pstat.addBatch();
			int[] rSet = pstat.executeBatch();
	        //execute query
	        //ResultSet rs = stmt.executeQuery(sql);
	        //position result to first      
	      }catch (Exception e){
	    	  System.err.println("Fail to execute SQL statement");
	    	  e.printStackTrace();
	      }
		}
	 }


