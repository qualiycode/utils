package com.qualiycode.utils.strings;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This is a Strings utility class
 * 
 * @author Eli Rozenfeld
 */
public class StringUtils {

	public static final String CRLF = "\r\n";
	
	/**
	 * @param str - string to look into
	 * @param regex - string to look for
	 * @return sub string using regular expression or null if not found
	 */
	public static String getSubstringUsingRegex(String str, String regex){
		return getSubstringUsingRegex(str, regex, 1);
	}
	
	/**
	 * @param str - string to look into
	 * @param regex - string to look for
	 * @param group - the regex group number (always starts from 1)
	 * @return sub string using regular expression or null if not found
	 */
	public static String getSubstringUsingRegex(String str, String regex, int group){
		return getSubstringUsingRegex(str, regex, group, 1);
	}

	/**
	 * @param str - string to look into
	 * @param regex - string to look for
	 * @param group - the regex group number (always starts from 1)
	 * @param matchNumber - the matching occurrence
	 * @return sub string using regular expression or null if not found
	 */
	public static String getSubstringUsingRegex(String str, String regex, int group, int matchNumber){
		return getSubstringUsingRegex(str, regex, group, matchNumber, false);
	}
	
	/**
	 * @param str - string to look into
	 * @param regex - string to look for
	 * @param group - the regex group number (always starts from 1)
	 * @param matchNumber - the matching occurrence
	 * @param isRegexCaseSensitive - if true the RegEx will match using case sensitive matchin
	 * @return sub string using regular expression or null if not found
	 */
	public static String getSubstringUsingRegex(String str, String regex, int group, int matchNumber, boolean isRegexCaseSensitive){
		int regexFlags = 0;//this means no flags
		
		if(!isRegexCaseSensitive){
			regexFlags = Pattern.CASE_INSENSITIVE;
		}
		
		Pattern pattern = Pattern.compile(regex, regexFlags);
		Matcher matcher = pattern.matcher(str);
		boolean matchFound = false;
		for(int i=0; i<matchNumber; i++){
			matchFound = matcher.find();
		}
		if (matchFound){
			if(matcher.groupCount() >= group){
				return matcher.group(group);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

		
	
	/**
	 * This function return the number of occurrences of a regex in a string
	 * @param str - the string to look inside
	 * @param regex - the regex to seek
	 * @return the number of occurrences of a regex in a string
	 */
	public static int getNumberOfOccurrences(String str, String regex){
		int num = 0;
		String match = getSubstringUsingRegex(str, regex, 0); 
		while(match != null){
			num = num + 1;
			str = str.replaceFirst("\\Q" + match + "\\E", "");
			match = getSubstringUsingRegex(str, regex, 0); 
		}
		return num;
	}
	
	/**
	 * This function gets the exception stack trace as string 
	 * @param e - the exception
	 * @return the exception stack trace as string
	 */
	public static String getExceptionStackTraceAsString(Throwable e){
		StringWriter stackTrace = new StringWriter();
		
		e.printStackTrace(new PrintWriter(stackTrace));
		
		return stackTrace.toString();
	}
}
