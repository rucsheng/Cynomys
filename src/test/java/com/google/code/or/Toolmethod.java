package com.google.code.or;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.code.db.Constant;

/**
 * @author ys
 * @usage This class contains some useful tool methods
 */
/**
 * @author ys
 *
 */
/**
 * @author ys
 *
 */
public class Toolmethod {
	
	/**
	 * @param input
	 * @return String with Apostrophe
	 */
	public static String addApos(String input) {
		String apos = "'";
		return apos + input + apos;
	}

    /**
     * @param input
     * @return String with Parentheses
     */
    public static String addPar(String input) {
		String lp = "(";
		String rp = ")";
		return lp + input + rp;
	}
    /**
     * @param regex
     * @param events
     * @return string macths given regex
     */
    public static String getRes(String regex, String events) {
    	try {
    	     Pattern p = Pattern.compile(regex);
    	     Matcher m = p.matcher(events);
    	     if (m.find()) {
    		     return m.group();
    	   }
    	} catch (Exception e){
    		System.err.println("Can not find such string");
    		return null;
    	} 
    	return null;
   }
    
    /**
     * @param TABLE_NAME
     * @param NumOfColumn
     * @return a string generate for INSERT preparestatement
     */
    public static String prepareINSERT(String TABLE_NAME, int NumOfColumn) {
    	int i = 0;
    	StringBuffer sBuffer = new StringBuffer("");
    	String qm = "?";
    	sBuffer.append("INSERT INTO ").append(TABLE_NAME).append(" VALUES (");
    	while(i < NumOfColumn - 1) {
    		sBuffer.append(qm).append(Constant.Comma);
    	}
    	sBuffer.append(qm).append(")");
    	return sBuffer.toString();
    }
}
