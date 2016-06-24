package com.qualiycode.utils.REST.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qualiycode.utils.strings.StringUtils;

/**
 * This is a generic class for working with REST API devices
 * 
 * To use this REST client just create an instance and use the handleHttpGetCommand() & handleHttpPostCommand() functions
 * 
 * @author Eli Rozenfeld
 *
 */
public class RestClient {

	protected final static Logger log = LoggerFactory.getLogger(RestClient.class);

	/**
	 * The REST server IP 
	 */
	String host = "";
	
	/**
	 * The REST server PORT 
	 */
	int port = 80;
	
	/**
	 * The connection type (for example: HTTP / HTTPs)
	 */
	RestConnectionType connectionType;
	
	/**
	 * The authentication type (for example: cookie / user & password) 
	 */
	RestAuthenticationMethod authenticationType;
	
	/**
	 * Authentication user name 
	 */
	String username;
	
	/**
	 * Authentication password 
	 */
	String password;
	
	/**
	 * Authentication cookie 
	 */
	String cookie = null;
	
	/**
	 * The HTTP REST Client 
	 */
	HttpClient httpClient;
	
	/**
	 * The last response status code 
	 */
	Integer lastStatusCode = null;
	
	final String SPRING_SECURITY_URI = "/login";
	
	/**
	 * The user name tag used when we are using cookie authentication 
	 */
	final String USER_IDENTIFIER = "username";
	
	/**
	 * The password tag used when we are using cookie authentication 
	 */
	final String PASSWORD_IDENTIFIER = "password";
	
	/**
	 * The HTTP header name which contains the cookie we get after authentication 
	 */
	final String COOKIE_RESPONSE_HEADER = "Set-Cookie";
	
	/**
	 * The HTTP header we use when we add the cookie to our requests 
	 */
	final String COOKIE_REQUEST_HEADER = "Cookie";
	
	
	/**
	 * This enum contains the supported RestClient HTTP request types
	 */
	private enum RestRequestType {
		POST,
		GET
	}
	
	/**
	 * @param host - the REST server IP
	 * @param port - the REST server PORT
	 * @param connectionType - the connection type (for example: HTTP / HTTPs)
	 * @throws Exception
	 */
	public RestClient(String host, int port, RestConnectionType connectionType) throws Exception{
		this(host, port, connectionType, RestAuthenticationMethod.NONE, null, null);
	}
	
	/**
	 * @param host - the REST server IP
	 * @param port - the REST server PORT
	 * @param connectionType - the connection type (for example: HTTP / HTTPs)
	 * @param authenticationType - the authentication type (for example: cookie / user & password)
	 * @param username - the authentication user
	 * @param password - the authentication password
	 * @throws Exception
	 */
	public RestClient(String host, int port, RestConnectionType connectionType, RestAuthenticationMethod authenticationType, String username, String password) throws Exception{
		this.host = host;
		this.port = port;
		this.connectionType = connectionType;
		this.authenticationType = authenticationType;
		this.username = username;
		this.password = password;
		httpClient = HttpClientBuilder.create().build();

		if(authenticationType == RestAuthenticationMethod.COOKIE){
			saveCookie();
		}
	}
	
	/**
	 * This function authenticate and get the authentication cookie from the REST server
	 * 
	 * Note: this cookie will be used for all HTTP requests
	 *  
	 * @throws Exception
	 */
	private void saveCookie() throws Exception{
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair(USER_IDENTIFIER, username));
		urlParameters.add(new BasicNameValuePair(PASSWORD_IDENTIFIER, password));
		
		URI httpFullUrl = buildFullHttpUrl(SPRING_SECURITY_URI, urlParameters);
		HttpRequestBase httpRequest = buildRequest(RestRequestType.POST, httpFullUrl, null);
		
		log.info("Getting authentication cookie");
		HttpResponse loginResponse = httpClient.execute(httpRequest);
		cookie = loginResponse.getFirstHeader(COOKIE_RESPONSE_HEADER).getValue();
		if(cookie == null || cookie.length() == 0){
			throw new Exception("Unable to get authentication cookie");
		}
		if (cookie.contains(";")) {
			cookie = cookie.substring(0, cookie.indexOf(";"));
		}
		log.info("Cookie = " + cookie);
		EntityUtils.consume(loginResponse.getEntity());
	}
	

	/**
	 * This function sends HTTP GET Request to the HTTP server and return the output as String
	 * 
	 * Notes:
	 * 1. The output is the content of the HTTP response
	 * 2. To get the HTTP response code you should use the getLastStatusCode() function after using this one
	 * 
	 * @param uri - the REST URI (for example: /api/metric.csv)
	 * @param urlParameters - the list of the request parameters
	 * @param headers - the list of the request headers
	 * @return the content of the HTTP response
	 * @throws Exception
	 */
	public String handleHttpGetCommand(String uri, List<NameValuePair> urlParameters, List<Header> headers) throws Exception{
		URI httpFullUri = buildFullHttpUrl(uri, urlParameters);
		log.info("Invoking HTTP GET request: " + httpFullUri.toURL());
		
		HttpRequestBase httpRequest = buildRequest(RestRequestType.GET, httpFullUri, headers);
		
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		
		lastStatusCode = httpResponse.getStatusLine().getStatusCode();
		StringBuilder responseContent = getResponseContent(httpResponse);

		EntityUtils.consume(httpResponse.getEntity());

		return responseContent.toString();
	}

	/**
	 * This function sends HTTP POST Request to the HTTP server and return the output as String
	 * 
	 * Notes:
	 * 1. The output is the content of the HTTP response
	 * 2. To get the HTTP response code you should use the getLastStatusCode() function after using this one
	 * 
	 * @param uri - the REST URI (for example: /api/metric.csv)
	 * @param urlParameters - the list of the request parameters
	 * @param headers - the list of the request headers
	 * @param contentType - the POST content type
	 * @param content - the POST content
	 * @return the content of the HTTP response
	 * @throws Exception
	 */
	public String handleHttpPostCommand(String uri, List<NameValuePair> urlParameters, List<Header> headers, ContentType contentType, byte[] content) throws Exception{
		URI httpFullUri = buildFullHttpUrl(uri, urlParameters);
		log.info("Invoking HTTP POST request: " + httpFullUri.toURL());
		
		HttpRequestBase httpRequest = buildRequest(RestRequestType.POST, httpFullUri, headers);

		HttpEntity entity = new ByteArrayEntity(content, contentType);
		((HttpPost)httpRequest).setEntity(entity);
		
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		
		lastStatusCode = httpResponse.getStatusLine().getStatusCode();
		StringBuilder responseContent = getResponseContent(httpResponse);

		EntityUtils.consume(httpResponse.getEntity());

		return responseContent.toString();
	}

	/**
	 * This function builds the full HTTP request URL
	 * @param uri - the REST URI (for example: /api/metric.csv)
	 * @param urlParameters - the list of the request parameters
	 * @return the full HTTP request URL
	 * @throws Exception
	 */
	private URI buildFullHttpUrl(String uri, List<NameValuePair> urlParameters) throws Exception{
		if(uri.endsWith("/")){
			uri = uri.substring(0, uri.length());
		}
		
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(connectionType.getUrlPrefix()).setHost(host).setPort(port).setPath(uri);
		if(urlParameters != null){
			uriBuilder.setParameters(urlParameters);
		}
		
		
		if(authenticationType == RestAuthenticationMethod.USER_PASSWORD){
			uriBuilder.setUserInfo(username, password);
		}
		
		return uriBuilder.build();
	}
	
	/**
	 * This function builds and return the HTTP request
	 * @param requestType - the HTTP request type (for example: GET / POST)
	 * @param httpFullUrl - the full HTTP request URL
	 * @param headers - the list of the request headers 
	 * @return the HTTP request
	 * @throws Exception
	 */
	private HttpRequestBase buildRequest(RestRequestType requestType, URI httpFullUrl, List<Header> headers) throws Exception{
		HttpRequestBase request = null;
		switch (requestType) {
		case GET:
			request = new HttpGet(httpFullUrl);
			break;

		case POST:
			request = new HttpPost(httpFullUrl);
			break;
		default:
			throw new Exception("Unsupported request type " + requestType.name());
		}
		
		if(authenticationType == RestAuthenticationMethod.COOKIE){
			request.addHeader(COOKIE_REQUEST_HEADER, cookie);
		}
		
		if(headers != null){
			for(Header header : headers){
				request.addHeader(header);
			}
		}
		return request;
	}
	
	/**
	 * This function extract the HTTP response content into a StringBuilder
	 * @param response - the HTTP response
	 * @return a StringBuilder containing the HTTP response content
	 * @throws Exception
	 */
	private StringBuilder getResponseContent(HttpResponse response) throws Exception{
		StringBuilder output = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			output.append(line + StringUtils.CRLF);
		}
		return output;
	}

	/**
	 * @return the last HTTP response status code
	 */
	public Integer getLastStatusCode() {
		return lastStatusCode;
	}
	
	
	//More Apache HTTP client examples: http://hc.apache.org/httpcomponents-client-ga/examples.html
}
