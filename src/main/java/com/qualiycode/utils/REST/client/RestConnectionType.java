package com.qualiycode.utils.REST.client;

/**
 * This enum contains the supported RestClient connection types
 * 
 * @author erozenfeld
 *
 */
public enum RestConnectionType {
	HTTP("http"),
	HTTPS("https");
	
	private final String urlPrefix;
	
	private RestConnectionType(String urlPrefix){
		this.urlPrefix = urlPrefix;
	}
	
	/**
	 * @return the connection URL prefix
	 */
	public String getUrlPrefix(){
		return urlPrefix;
	}
}
