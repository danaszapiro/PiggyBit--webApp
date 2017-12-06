package com.piggybit.models;

import java.util.ArrayList;
import java.util.List;

public class eventLog {
	private long moneyInvested;
	private String currency;
	private String cryptocurrency;
	private int period;
	private String date;

	protected eventLog() {
	}

	public eventLog(long moneyInvested, String currency, String cryptocurrency, int period, String date) {
		this.moneyInvested = moneyInvested;
		this.currency = currency;
		this.cryptocurrency = cryptocurrency;
		this.period = period;
		this.date = date;
	}

}
