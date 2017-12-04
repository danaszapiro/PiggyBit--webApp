package com.demo.coinbaseAPI;

public class SettingsForm {

    private String currency;
    private String preferredCrypto;
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
	
	public String getPreferredCrypto() {
		return preferredCrypto;
	}

	public void setPreferredCrypto(String preferredCrypto) {
		this.preferredCrypto = preferredCrypto;
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