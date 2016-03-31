package com.qualiycode.utils.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

/**
 * This class implements the logic of parsing a CSV file / String according to a desired column enum
 *
 * @author Eli Rozenfeld
 *
 */
public class CsvParser {

	protected CsvColumnsInterface[] csvColumns;

	protected String lineDelimiter;

	protected String columnDelimiterSupportQuotes;

	protected String columnDelimiter;

	protected File localCsvFileLocation;

	protected final String DEFAULT_COLUMN_DELIMITER = ",";
	protected final String DEFAULT_LINE_DELIMITER = "\n";

	protected String csvStringContent = null;
	
	/**
	 * @param csvLocation - the full path of file on the local machine
	 * @param csvColumns - the values of the enum used for parsing the CSV file
	 */
	public CsvParser(File csvLocation, CsvColumnsInterface[] csvColumns){
		this.localCsvFileLocation = csvLocation;
		this.csvColumns = csvColumns;
		setColumnDelimiter(DEFAULT_COLUMN_DELIMITER, true);
		lineDelimiter = DEFAULT_LINE_DELIMITER;
		csvStringContent = null;
	}

	/**
	 * Use this structor to parse CSV data in string object
	 * @param csvStringContent - the CSV data in String object
	 * @param csvColumns - the values of the enum used for parsing the CSV file
	 */
	public CsvParser(String csvStringContent, CsvColumnsInterface[] csvColumns){
		this((File)null, csvColumns);
		this.csvStringContent = csvStringContent;
	}

	/**
	 * This function return the enum value according to its String value
	 * @param colomnName
	 * @return
	 */
	protected CsvColumnsInterface getColomnEnumByName(String colomnName){
		for(CsvColumnsInterface colomn : csvColumns){
			if(colomn.getCsvColumnName().equalsIgnoreCase(colomnName.trim())){
				return colomn;
			}
		}
		return null;
	}

	/**
	 * This function pull the CSV file and parse it
	 * @return an HashMap containing each column and its lines
	 * @throws Exception
	 */
	public HashMap<CsvColumnsInterface, ArrayList<String>> getParsedCsvFile() throws Exception{
		return getParsedCsvFile(false);
	}

	/**
	 * This function pull the CSV file and parse it
	 * @param isRemoveEndOfLines - if true, removes \n\r from each values in the parsed file
	 * @return an HashMap containing each column and its lines
	 * @throws Exception
	 */
	public HashMap<CsvColumnsInterface, ArrayList<String>> getParsedCsvFile(boolean isRemoveEndOfLines) throws Exception {

		if(csvStringContent == null){
			csvStringContent = FileUtils.readFileToString(localCsvFileLocation);
		}

		String[] fileLines = csvStringContent.split(lineDelimiter);

		HashMap<CsvColumnsInterface, ArrayList<String>> csvContent = new HashMap<>();

		int numberOfColumns = fileLines[0].split(columnDelimiterSupportQuotes).length;

		String[] fileColomns;
		CsvColumnsInterface colomn;

		for(int i=0; i<numberOfColumns; i++){
			fileColomns = fileLines[0].split(columnDelimiterSupportQuotes);
			colomn = getColomnEnumByName(fileColomns[i]);
			if(colomn != null){
				csvContent.put(colomn, new ArrayList<String>());
				for(int j=1; j<fileLines.length; j++){
					if(fileLines[j].endsWith(columnDelimiter)){
						fileLines[j] = fileLines[j].concat(" ");
					}
					fileColomns = fileLines[j].split(columnDelimiterSupportQuotes);
					
					//if the value contains "," or enclosed with quotes (like this "blabla") we remove the qutes
					fileColomns[i] = fileColomns[i].trim();
					if(fileColomns[i].contains(DEFAULT_COLUMN_DELIMITER) || (fileColomns[i].startsWith("\"") && fileColomns[i].replaceAll("[\n\r]", "").endsWith("\""))){
						fileColomns[i] = fileColomns[i].replaceAll("\"", "");
					}
					
					if(isRemoveEndOfLines){
						csvContent.get(colomn).add(fileColomns[i].replaceAll("[\n\r]", ""));
					}else{
						csvContent.get(colomn).add(fileColomns[i]);
					}
				}
			}
		}
		return csvContent;
	}

	/**
	 * @return the current delimiter used as line delimiter
	 */
	public String getLineDelimiter() {
		return lineDelimiter;
	}

	/**
	 * @param lineDelimiter - the line delimiter
	 */
	public void setLineDelimiter(String lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}

	/**
	 * @return the current delimiter used as column delimiter
	 */
	public String getColumnDelimiter() {
		return columnDelimiterSupportQuotes;
	}

	/**
	 * @param columnDelimiter - the column delimiter
	 */
	public void setColumnDelimiter(String columnDelimiter) {
		setColumnDelimiter(columnDelimiter, true);
	}

	/**
	 * @param columnDelimiter - the column delimiter
	 * @param ignoreDelimiterInsideQuotes - if true will ignore delimiter inside quotes
	 */
	public void setColumnDelimiter(String columnDelimiter, boolean ignoreDelimiterInsideQuotes) {
		this.columnDelimiter = columnDelimiter;
		if(ignoreDelimiterInsideQuotes){
			columnDelimiterSupportQuotes = columnDelimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
		}else{
			columnDelimiterSupportQuotes = columnDelimiter;
		}

		/*
		The logic of ignoring quotes is:
		    split on the comma only if that comma has zero, or an even number of quotes in ahead of it.
		    code took from: http://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
		*/
	}
	
}
