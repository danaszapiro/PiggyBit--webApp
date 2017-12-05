package com.coinbase.apis;

import com.coinbase.services.CoinbaseAccessTokenJsonExtractor;
import com.coinbase.services.CoinbaseService;
import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;

/**
 * coinbase API
 */
public class CoinbaseApi extends DefaultApi20 {

	protected CoinbaseApi() {
	}

	private static class InstanceHolder {

		private static final CoinbaseApi INSTANCE = new CoinbaseApi();
	}

	public static CoinbaseApi instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.GET;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "http://www.coinbase.com/oauth/token";
	}

	@Override
	public String getRefreshTokenEndpoint() {
		throw new UnsupportedOperationException("coinbase doesn't support refreshing tokens");
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return "https://www.coinbase.com/oauth/authorize";
	}

	@Override
	public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
		return CoinbaseAccessTokenJsonExtractor.instance();
	}

	@Override
	public ClientAuthenticationType getClientAuthenticationType() {
		return ClientAuthenticationType.REQUEST_BODY;
	}

	@Override
	public CoinbaseService createService(OAuthConfig config) {
		return new CoinbaseService(this, config);
	}
}