package com.coinbase.authenticatedAPIcalls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Buys {

	public final static String HTTP_URL = "https://api.coinbase.com/v2/accounts/";

	public static String makeABuy(String accessToken, double amount, String crypto, String accountId) throws IOException {
		HttpClient httpclient = HttpClientBuilder.create().build(); // the http-client, that will send the request
		HttpPost httpPost = new HttpPost(HTTP_URL + accountId + "/buys"); // the http GET request
		httpPost.addHeader("Authorization", "Bearer " + accessToken); // add the authorization header to the request
		httpPost.addHeader("Content-Type", "application/json");
		JSONObject json = new JSONObject();
		json.put("amount", String.valueOf(amount));
		json.put("currency", crypto);
		json.put("commit", false);
		json.put("query", true);
		StringEntity params = new StringEntity(json.toString());
		httpPost.setEntity(params);
	    //httpPost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = httpclient.execute(httpPost); // the client executes the request and gets a response
		int responseCode = response.getStatusLine().getStatusCode(); // check the response code
		String stringResponse = "";
		switch (responseCode) {
		case 200: {
			// everything is fine, handle the response
			stringResponse = EntityUtils.toString(response.getEntity()); // now you have the response as String, which
																			// you can convert to a JSONObject or do
			System.out.println(responseCode);// other stuff
			break;
		}
		case 500: {
			// server problems ?
			System.out.println(responseCode);
			break;
		}
		case 403: {
			System.out.println(responseCode);
			break;
		}
		}
		return stringResponse;

	}

	public static String makeABuy(String input) {
		return input;
		
	}
}
