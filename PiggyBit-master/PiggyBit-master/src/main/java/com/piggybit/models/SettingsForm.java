package com.piggybit.models;

public class SettingsForm {

	private String currency;
	private String crypto;
	private String priceMargin;
	private String recurringPeriod;

	public SettingsForm() {
		// TODO Auto-generated constructor stub
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

	public String getPriceMargin() {
		return priceMargin;
	}

	public void setPriceMargin(String priceMargin) {
		this.priceMargin = priceMargin;
	}

	public String getRecurringPeriod() {
		return recurringPeriod;
	}

	public void setRecurringPeriod(String recurringPeriod) {
		this.recurringPeriod = recurringPeriod;
	}

}