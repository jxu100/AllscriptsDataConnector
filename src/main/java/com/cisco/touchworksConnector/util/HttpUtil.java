package com.cisco.touchworksConnector.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.log4j.Logger;


public class HttpUtil {

	static Logger log = Logger.getLogger(HttpUtil.class);
	
	private static final int CONNECT_TIMEOUT =10000;

	public static String invokeHttpGet(String uri) throws Exception {

	   String responseBody = "";
	  
	   RequestConfig config = RequestConfig.custom()
	     .setConnectTimeout(CONNECT_TIMEOUT)
	     .setConnectionRequestTimeout(CONNECT_TIMEOUT)
	     .setSocketTimeout(CONNECT_TIMEOUT).build();
	   
	   CloseableHttpClient client = 
			     HttpClientBuilder.create().setDefaultRequestConfig(config).build();


	   try {
		 
	    		HttpUriRequest request = RequestBuilder.get()
	    		  .setUri(uri)
	    		//  .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
	    		  .build();
		
	    		ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = client.execute(request, responseHandler);
			
		} catch (Exception e) {
			log.error("invokeHttpGet - Exception exception "
					+ "while executing http get req:" + e.getMessage());
		
		} finally {
            client.close();
        }

		log.debug("invokeHttpGet - response:" + responseBody);

		return responseBody;
	}

	public static String invokeHttpPost(String uri, String payload, String contentType) throws Exception{
		log.info(uri + ":" + payload + ":" + contentType);
		
		RequestConfig config = RequestConfig.custom()
			     .setConnectTimeout(CONNECT_TIMEOUT)
			     .setConnectionRequestTimeout(CONNECT_TIMEOUT)
			     .setSocketTimeout(CONNECT_TIMEOUT).build();
			   
		CloseableHttpClient client = 
					     HttpClientBuilder.create().setDefaultRequestConfig(config).build();


		StringEntity entity = null;
		try {
			entity = new StringEntity(payload);
		} catch (Exception e) {
			log.error("invokeHttpPost - unsupported string entity exception "
					+ "while executing http post req:" + e.getMessage());
			return "";
		}
		
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";

		try {
			HttpUriRequest request = RequestBuilder.post()
		    		  .setUri(uri)
		    		  .addHeader(HttpHeaders.CONTENT_TYPE, contentType)
		    		  .setEntity(entity)
		    		  .build();
			HttpResponse response = client.execute(request);
			responseBody = IOUtils.toString(response.getEntity().getContent());

		} catch (ClientProtocolException e) {
			log.error("invokeHttpPost - ClientProtocolException exception "
					+ "while executing http post req:" + e.getMessage());
			
		} catch (IOException e) {
			log.error("invokeHttpPost - IOException exception "
					+ "while executing http post req:" + e.getMessage());
		
		} catch (Exception e) {
			log.error("invokeHttpPost - Exception exception "
					+ "while executing http post req:" + e.getMessage());
		}finally {
            client.close();
        }

		log.debug("invokeHttpPost - response:" + responseBody);

		return responseBody;
	}

	
}
