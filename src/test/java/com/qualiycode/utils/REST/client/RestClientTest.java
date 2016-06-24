package com.qualiycode.utils.REST.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RestClientTest {

	/**
	 * This test validate the REST client operation by connecting to google play store and fetching some data
	 * @throws Exception
	 */
	@Test
	public void restClientTest() throws Exception{
		Logger logger = Logger.getRootLogger();
		logger.removeAllAppenders();
		logger.setLevel(Level.INFO);
		logger.addAppender(new ConsoleAppender(new PatternLayout("%d{ISO8601} [%p] %C.%M:%L- %m%n")));

		RestClient rc = new RestClient("play.google.com", 80, RestConnectionType.HTTP);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", "com.whatsapp"));
		
		String output = rc.handleHttpGetCommand("/store/apps/details", params, null);
		Assert.assertTrue(rc.getLastStatusCode() == 200);
		Assert.assertTrue(output.length() > 0);
	}

}
