package com.piggybit.models;

import java.util.ArrayList;
import java.util.List;

public class EventLog {
	private double moneyInvested;
	private String currency;
	private String cryptocurrency;
	private int period;

	protected EventLog() {
	}

	public EventLog(double moneyInvested, String currency, String cryptocurrency, int period, String date) {
		this.moneyInvested = moneyInvested;
		this.currency = currency;
		this.cryptocurrency = cryptocurrency;
		this.period = period;
	}

}
