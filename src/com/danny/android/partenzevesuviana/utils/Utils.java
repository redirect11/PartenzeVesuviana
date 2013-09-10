package com.danny.android.partenzevesuviana.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Utils {

	public static Date stringToDate(String date) {
		 DateFormat df = new SimpleDateFormat("HH:mm");
		 try {
	            Date today = df.parse(date);
	            //System.out.println("Today = " + df.format(today));
	            return today;
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		return null;
	}
	
	public static String dateToString(Date date) {
		DateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(date);
	}
	
	public static String getCat(String cat) {
		String excat = "Non pervenuto";
		if(cat.equals("A")) {
			excat = "Accelerato";
		} else if (cat.equals("d")) {
			 excat = "Diretto";
		} else if (cat.equals("D")) {
			excat = "Direttissimo";
		}
		return excat;
	}
	
	public static ArrayList<String> removeDuplicate(ArrayList<String> list) {
		HashSet<String> set = new HashSet<String>(list);
		ArrayList<String> newList = new ArrayList<String>(set);
		return newList;
	}
}
