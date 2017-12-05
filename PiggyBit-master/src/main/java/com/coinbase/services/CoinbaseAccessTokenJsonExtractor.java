package com.coinbase.services;

import com.coinbase.errors.CoinbaseAccessTokenErrorResponse;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

import java.util.regex.Pattern;

/**
 * non standard Coinbase Extractor
 */
public class CoinbaseAccessTokenJsonExtractor extends OAuth2AccessTokenJsonExtractor {

	private static final Pattern INSTANCE_URL_REGEX_PATTERN = Pattern.compile("\"instance_url\"\\s*:\\s*\"(\\S*?)\"");

	protected CoinbaseAccessTokenJsonExtractor() {
	}

	private static class InstanceHolder {

		private static final CoinbaseAccessTokenJsonExtractor INSTANCE = new CoinbaseAccessTokenJsonExtractor();
	}

	public static CoinbaseAccessTokenJsonExtractor instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	protected CoinbaseToken createToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken,
			String scope, String response) {
		return new CoinbaseToken(accessToken, tokenType, expiresIn, refreshToken, scope,
				extractParameter(response, INSTANCE_URL_REGEX_PATTERN, true), response);
	}
}