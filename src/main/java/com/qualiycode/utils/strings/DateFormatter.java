package com.qualiycode.utils.strings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a Date formatting utility class
 * 
 * @author Eli Rozenfeld
 */
public class DateFormatter {

	/**
	 * This function format a date according to a desired format
	 * @param date - data to be formatted
	 * @param originalFormat - the original date format
	 * @param desiredFormat - the desired date format
	 * @return the date formated according to the desired format
	 * @throws Exception
	 */
	public static String getFormattedDate(String date, String originalFormat, String desiredFormat) throws Exception{
		//converting date format
		DateFormat formatter = new SimpleDateFormat(originalFormat); 
		Date newDateFormat = (Date)formatter.parse(date); 
		formatter = new SimpleDateFormat(desiredFormat);
		return formatter.format(newDateFormat);		
	}
	
	/**
	 * This function format a date according to a desired format
	 * @param date - data to be formatted
	 * @param originalFormat - the original date format
	 * @param desiredFormat - the desired date format
	 * @return the date formated according to the desired format
	 * @throws Exception
	 */
	public static String getFormattedDate(String date, DateFormat originalFormat, DateFormat desierdFormat) throws Exception{
		Date newDateFormat = (Date)originalFormat.parse(date);
		return desierdFormat.format(newDateFormat);
	}
	
}
