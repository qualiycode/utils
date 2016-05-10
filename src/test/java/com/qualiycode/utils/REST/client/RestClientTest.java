package com.qualiycode.utils.REST.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.qualiycode.utils.strings.DateFormatter;
import com.qualiycode.utils.strings.StringUtils;

@RunWith(JUnit4.class)
public class RestClientTest {

	/**
	 * This test validate the REST client operation by connecting to google play store and fetching some data
	 * @throws Exception
	 */
	@Test
	public void restClientTest() throws Exception{
		
		RestClient rc = new RestClient("play.google.com", 80, RestConnectionType.HTTP);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", "com.whatsapp"));
		
		String output = rc.handleRestCommand("/store/apps/details", RestRequestType.GET, params, null);
		Assert.assertTrue(rc.getLastStatusCode() == 200);
		
		//extracting published date 
		String published = StringUtils.getSubstringUsingRegex(output, "datePublished.*?\\>([\\w\\d\\s\\,]+)\\<");
		
		published = DateFormatter.getFormattedDate(published, "MMM dd, yyyy", "dd/MM/yyyy");
		
		System.out.println("Application = " + "Whatsapp");
		System.out.println("Application Id on Google Play Store = " + "com.whatsapp");
		System.out.println("published = " + published);
		
	}

}
