package com.piggybit.models;

public class SettingsForm {

	private String currency;
	private String crypto;
	private int priceMargin;
	private int recurringPeriod;

	public SettingsForm() {
		// TODO Auto-generated constructor stub
	}

	public SettingsForm(String currency, String crypto, int priceMargin, int recurringPeriod) {
		this.currency = currency;
		this.crypto = crypto;
		this.priceMargin = priceMargin;
		this.recurringPeriod = recurringPeriod;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCrypto() {
		return crypto;
	}

	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}

	public int getPriceMargin() {
		return priceMargin;
	}

	public void setPriceMargin(int priceMargin) {
		this.priceMargin = priceMargin;
	}

	public int getRecurringPeriod() {
		return recurringPeriod;
	}

	public void setRecurringPeriod(int recurringPeriod) {
		this.recurringPeriod = recurringPeriod;
	}

}