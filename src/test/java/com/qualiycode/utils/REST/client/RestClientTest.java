package com.qualiycode.utils.REST.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.qualiycode.utils.pair.Pair;
import com.qualiycode.utils.strings.DateFormatter;
import com.qualiycode.utils.strings.StringUtils;

@RunWith(JUnit4.class)
public class RestClientTest {

	/**
	 * Holds the list of applications and their Google Play Store ID's 
	 */
	private ArrayList<Pair<String, String>> applications;
	
	/**
	 * Fill list of applications and their Google Play Store ID's
	 */
	private void fillApplicationAndIds(){
		applications = new ArrayList<Pair<String, String>>();
		applications.add(new Pair<String, String>("WhatsApp", "com.whatsapp"));
		applications.add(new Pair<String, String>("Facebook messenger", "com.facebook.orca"));
		applications.add(new Pair<String, String>("Viber", "com.viber.voip"));
		applications.add(new Pair<String, String>("Skype", "com.skype.raider"));
	}
	
	/**
	 * This test validate the REST client operation by connecting to google play store and fetching some data
	 * @throws Exception
	 */
	@Test
	public void restClientTest() throws Exception{
		fillApplicationAndIds();
		
		RestClient rc = new RestClient("play.google.com", 80, RestConnectionType.HTTP);

		for(Pair<String, String> app : applications){

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", app.getSecond()));
			
			String output = rc.handleRestCommand("/store/apps/details", RestRequestType.GET, params, null);
			Assert.assertTrue(rc.getLastStatusCode() == 200);
			
			//extracting date & supported OS version
			String osVersions = StringUtils.getSubstringUsingRegex(output, "operatingSystems.*?\\>\\s+([\\w\\.\\s]+)\\<");
			String published = StringUtils.getSubstringUsingRegex(output, "datePublished.*?\\>([\\w\\d\\s\\,]+)\\<");
			
			published = DateFormatter.getFormattedDate(published, "MMM dd, yyyy", "dd/MM/yyyy");
			
			System.out.println("*****************************************");
			System.out.println("Application = " + app.getFirst());
			System.out.println("Application Id on Google Play Store = " + app.getSecond());
			System.out.println("published = " + published);
			System.out.println("OS versions = " + osVersions);
		}
		
	}

}
