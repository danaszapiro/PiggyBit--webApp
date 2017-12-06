package com.coinbase.apis;

import java.io.IOException;
import java.util.Collections;

import com.coinbase.api.entity.TokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class CoinbaseOauth2 {
	public static Credential authorize() throws Exception { 
	JsonFactory jsonFactory = new JacksonFactory();
	HttpTransport httpTransport = new NetHttpTransport();
	 
	AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
	    BearerToken.authorizationHeaderAccessMethod(),
	    httpTransport, jsonFactory,
	    new GenericUrl("http://www.coinbase.com/oauth/token"),
	    new ClientParametersAuthentication("778c0d1ec98e98f35c4fdd30d83cb813735ae7afa0ff13f8e74c84311d4f80b8", 
	    		"8c936687c9bd55712f15e962ed84f32811594c41e8f3cc69ea458445d539729b"), 
	    "778c0d1ec98e98f35c4fdd30d83cb813735ae7afa0ff13f8e74c84311d4f80b8",
	    "https://www.coinbase.com/oauth/authorize")
			.setScopes(Collections.singletonList("wallet:user:email,wallet:user:read,wallet:buys:read,wallet:buys:create,wallet:accounts:read,wallet:transactions:read"))
			.build();

	   LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(
		        "localhost").setPort(8084).build();
	  return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		    
	
}
	}
