package com.google.code.or;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ys
 * @usage This class contains some useful tool methods
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
}
