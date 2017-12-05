package com.coinbase.services;

import java.util.Objects;

import com.github.scribejava.core.model.OAuth2AccessToken;

public class CoinbaseToken extends OAuth2AccessToken {

	private static final long serialVersionUID = 7496092256264195577L;

	/**
	 * This token model includes the instance_url to address the needed Coinbase
	 * organization instance.
	 */
	private final String instanceUrl;

	public CoinbaseToken(String accessToken, String instanceUrl, String rawResponse) {
		this(accessToken, null, null, null, null, instanceUrl, rawResponse);
	}

	public CoinbaseToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope,
			String instanceUrl, String rawResponse) {
		super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
		this.instanceUrl = instanceUrl;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 37 * hash + Objects.hashCode(instanceUrl);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return Objects.equals(instanceUrl, ((CoinbaseToken) obj).getInstanceUrl());
	}
}