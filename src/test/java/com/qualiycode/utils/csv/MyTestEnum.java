package com.qualiycode.utils.csv;

/**
 * This enum is used for describing the CSV data used in the CsvParser unit tests 
 * 
 * @author Eli Rozenfeld
 */
public enum MyTestEnum implements CsvColumnsInterface {
	COLUMN_A("column_A"),
	COLUMN_B("column_B"),
	COLUMN_C("column_C"),
	COLUMN_D("column_D"),
	COLUMN_E("column_E");
	
	private final String columnName;
	
	private MyTestEnum(String columnName){
		this.columnName = columnName;
	}
	
	
	@Override
	public String getCsvColumnName() {
		return columnName;
	}

}
