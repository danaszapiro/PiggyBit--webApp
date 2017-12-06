package com.coinbase.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class TokenExtractor {

	public static final String CB_APP_ID = "778c0d1ec98e98f35c4fdd30d83cb813735ae7afa0ff13f8e74c84311d4f80b8";
	public static final String CB_APP_SECRET = "8c936687c9bd55712f15e962ed84f32811594c41e8f3cc69ea458445d539729b";
	public static final String REDIRECT_URI = "http://localhost:8084/oauth_code/";
	public static final String OAUTH_URL = "https://api.coinbase.com/oauth/token";

	public static String getToken(String code) throws IOException {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "&grant_type=authorization_code" + "&code=" + code
				+ "&client_id=" + CB_APP_ID + "&client_secret=" + CB_APP_SECRET + "&redirect_uri=" + REDIRECT_URI);
		Request request = new Request.Builder().url(OAUTH_URL).post(body).build();

		Response response = client.newCall(request).execute();
		return response.body().string();

	}

	public static String getAccessToken(String token) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(token);
		String access = (String) json.get("access_token");
		return access;
	}

	public static String getRefreshToken(String token) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(token);
		String refresh = (String) json.get("refresh_token");
		return refresh;
	}

	public static String refreshTheToken(String token) throws IOException {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "&grant_type=refresh_token" 
				+ "&client_id=" + CB_APP_ID + "&client_secret=" + CB_APP_SECRET + "&refresh_token=" + token);
		Request request = new Request.Builder().url(OAUTH_URL).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();

	}
}
