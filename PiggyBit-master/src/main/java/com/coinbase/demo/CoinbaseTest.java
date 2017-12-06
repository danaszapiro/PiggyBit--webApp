package com.coinbase.demo;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.apis.CoinbaseApi;
import com.coinbase.authenticatedAPIcalls.Accounts;
import com.coinbase.authenticatedAPIcalls.Users;
import com.coinbase.services.TokenExtractor;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.simple.parser.ParseException;

public final class CoinbaseTest {

	private static final String NETWORK_NAME = "Coinbase";
	private static final String AUTHORIZE_URL = "https://www.coinbase.com/oauth/authorize";
	private static final String SCOPE = "wallet:user:email,wallet:user:read,wallet:buys:read,"
			+ "wallet:buys:create,wallet:accounts:read,wallet:transactions:read";

	private CoinbaseTest() {
	}

	public static String getAuth()
			throws IOException, InterruptedException, ExecutionException, CoinbaseException, ParseException {
		final OAuth20Service service = new ServiceBuilder(
				"778c0d1ec98e98f35c4fdd30d83cb813735ae7afa0ff13f8e74c84311d4f80b8")
						.apiSecret("8c936687c9bd55712f15e962ed84f32811594c41e8f3cc69ea458445d539729b").scope(SCOPE)
						.callback("http://localhost:8084/oauth_code/").build(CoinbaseApi.instance());
		final Scanner in = new Scanner(System.in);

		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();

		// Obtain the Authorization URL if user does not exist
		System.out.println("Fetching the Authorization URL...");
		final String authorizationUrl = service.getAuthorizationUrl();
		return authorizationUrl;

	}

	public static String getTokens(String code) throws IOException, ParseException {
		System.out.println();

		// Trade the Authorization Code for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		String token = TokenExtractor.getToken(code);
		System.out.println("Got the Access Token!");
		System.out.println("(if you're curious the raw answer looks like this: " + token + "')");
		System.out.println();

		// else pass in refresh token for new access token

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");

		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		String accessToken = TokenExtractor.getAccessToken(token);
		String test = Accounts.getAccounts(Accounts.getAccountInfo(accessToken));
		return test;
	}
}