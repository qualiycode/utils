package com.qualiycode.utils.email;

/**
 * This enum contain list of content types
 * 
 * @author Eli Rozenfeld
 *
 */
public enum ContentType {
	TXT("text/plain"),
	HTML("text/HTML");
	
	private String contentType;
	
	private ContentType(String contentType){
		this.contentType = contentType;
	}
	
	/**
	 * @return the content type string
	 */
	public String getContentType(){
		return contentType;
	}
	
}
