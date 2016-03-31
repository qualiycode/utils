package com.qualiycode.utils.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CsvParsingTest {

	/**
	 * Prefix used when generating dummy CSV content
	 */
	private final String CSV_DATA_PREFIX = "_Value";
	
	/**
	 * This function generates dummy CSV data
	 * @param csvColumns - the CSV columns
	 * @param numberOfLines - The number of lines to generate
	 * @return CSV data in a String object
	 */
	protected String generateCsvContent(CsvColumnsInterface[] csvColumns, int numberOfLines){
		StringBuffer line;
		StringBuffer lines = new StringBuffer();
		
		for(int i=0; i<numberOfLines; i++){
			line = new StringBuffer();
			for(CsvColumnsInterface column : csvColumns){
				if(i==0){
					//The first line of CSV which contains the column names
					line.append(column.getCsvColumnName() + ",");
				}else{
					//The CSV lines which contains the actual data
					line.append(column.getCsvColumnName() + CSV_DATA_PREFIX + i + ",");
				}
			}
			lines.append(line.substring(0, line.length()-1) + "\r\n");//removing last ","
		}
		return lines.toString();
	}
	
	/**
	 * This function validates the CSV data extracted using the CSV parser
	 * @param csvData - the CSV data extracted using the CSV parser
	 * @throws Exception
	 */
	public void validateCsvContent(HashMap<CsvColumnsInterface, ArrayList<String>> csvData) throws Exception{
		for(CsvColumnsInterface column : csvData.keySet()){
			ArrayList<String> specificColumnLines = csvData.get(column);
			
			for(int i=0; i<specificColumnLines.size(); i++){
				Assert.assertTrue(specificColumnLines.get(i).equals(column.getCsvColumnName() + CSV_DATA_PREFIX + (i+1)));
			}
		}
	}
	
	/**
	 * This test validate the CsvParser operation using CSV data in String object
	 * @throws Exception
	 */
	@Test
	public void csvParsingFromStringTest() throws Exception{
		String csvContent = generateCsvContent(MyTestEnum.values(), 10);
		CsvParser csvParser = new CsvParser(csvContent, MyTestEnum.values());
		HashMap<CsvColumnsInterface, ArrayList<String>> csvData = csvParser.getParsedCsvFile();

		validateCsvContent(csvData);
	}

	/**
	 * This test validate the CsvParser operation using CSV data in file
	 * @throws Exception
	 */
	@Test
	public void csvParsingFromFileTest() throws Exception{
		String csvContent = generateCsvContent(MyTestEnum.values(), 10);
		File csvFile = new File("myCsvFile.csv");

		//write csvContent to file
		FileUtils.writeStringToFile(csvFile, csvContent, false);
		
		CsvParser csvParser = new CsvParser(csvFile, MyTestEnum.values());
		HashMap<CsvColumnsInterface, ArrayList<String>> csvData = csvParser.getParsedCsvFile();
		
		//delete the csv file
		FileUtils.forceDelete(csvFile);

		validateCsvContent(csvData);
		
	}

}
