package com.qualiycode.utils.enconding.w64;

import org.apache.commons.net.util.Base64;

/**
 * This class handles the encoding & decoding of Strings to W64 representation
 * 
 * @author Eli Rozenfeld
 *
 */
public class W64Convertor {

	/**
	 * This function encode a string to W64 string
	 * @param originalString - the regular string
	 * @return the W64 string representing the regular string
	 * @throws Exception
	 */
	public static String getEncodedW64String(String originalString) throws Exception{
		try {
			return new String(Base64.encodeBase64(originalString.getBytes()));
		} catch (Exception e) {
			throw new Exception("Unable to perform W64 encoding to: " + originalString);
		}
	}
	
	/**
	 * This function decode a w64 string to a string
	 * @param w64EncodedString - the W64 encoded string
	 * @return the string representing the W64 encoded string
	 * @throws Exception
	 */
	public static String getDecodedW64String(String w64EncodedString) throws Exception{
		try {
			byte[] decode = Base64.decodeBase64(w64EncodedString);
			return new String(decode);
		} catch (Exception e) {
			throw new Exception("Unable to perform W64 decoding to: " + w64EncodedString);
		}
	}
}
