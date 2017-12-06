package com.yodlee.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yodlee.beans.UserContext;
import com.yodlee.parser.GSONParser;
import com.yodlee.utils.HTTP;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.piggybit.models.User;

public class GetTransactions {
	
	private String localURLVer1 = "https://developer.api.yodlee.com/ysl/restserver/";
    String cobSession = "";
    String userSession = "";
    String userAccounts = "";
    String cobrandLogin = "sbCobd74e196a9cfac3ff5c74d3ee313bf2de6a";
    String cobrandPassword = "1734ef64-b099-492c-8797-b0b00c40a495";
    //String accountId = "";
    String transactions = ""; 
    //String date_str = "2013-12-01";
    double investment_total = 0.00;
    double margin = 10.00;
    
    Date date = new Date();
    String date_str = String.format("%tF", date);
    //System.out,println("Current date:");
    //System.out.println("Current date: " + date_str);
    
    
    
    public String userLogin(String cobSession, String userName, String password) {

		String userSession = null;

		try {
			Map<String, String> loginTokens = new HashMap<String, String>();
			loginTokens.put("cobSession", cobSession);

			// User login
			String userLoginURL = "https://developer.api.yodlee.com/ysl/restserver/" + "v1/user/login";

			//Request Body JSON - refer to full API reference at https://developer.yodlee.com/apidocs/index.php
			final String requestBody2 = "{" 
								+ "\"user\":{"
								+ "\"loginName\":\"" + userName + "\"" + ","
								+ "\"password\":\"" + password + "\"" + ","
								+ "\"locale\": \"en_US\"" 
								+ "}" 
							+ "}";
			

			String userjsonResponse = HTTP.doPostUser(userLoginURL,
					loginTokens, requestBody2, true);
			UserContext member = (UserContext) GSONParser.handleJson(
					userjsonResponse, com.yodlee.beans.UserContext.class);

			if (!userjsonResponse.contains("errorCode")) {
				userSession = member.getUser().getSession().getUserSession();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userSession;
	}
        
    public String getUserAccounts(String cobSession, String userSession) {
		String accountURL = "https://developer.api.yodlee.com/ysl/restserver/" + "v1/accounts";
		Map<String,String> loginTokens = new HashMap<String,String>();
		loginTokens.put("cobSession", cobSession);
		loginTokens.put("userSession",userSession);
		
		String accountJsonResponse=null;
		
		try {
			accountJsonResponse = HTTP.doGet(accountURL,loginTokens);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountJsonResponse;
	}
    
    public String getTransactions(String cobSession, String userSession,
			String accountId, String date) {
		
		String txnJson ="";
		String TransactionUrl = "https://developer.api.yodlee.com/ysl/restserver/" + "v1/transactions" + "?fromDate=" + date + "&accountId="+accountId;

		try {
			Map<String,String> loginTokens = new HashMap<String,String>();
			loginTokens.put("cobSession", cobSession);	
			loginTokens.put("userSession",userSession);
			
			txnJson = HTTP.doGet(TransactionUrl,loginTokens);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return txnJson;
	}
    
    //Return the investment amount where userAccounts is string obtained from getUserAccounts function
    public double getInvestment( String userAccounts, String cobSession, String userSession, 
    		Date most_recent_investment, Date current_time, double margin) throws ParseException { 
    		
    		Double investment_total = 0.00;
    		
    		JSONParser parser = new JSONParser();
        JSONObject userAccount = (JSONObject) parser.parse(userAccounts);
        JSONArray accountList = (JSONArray) userAccount.get("account");
        System.out.println("Number of accounts: " +  accountList.size());
        JSONObject account = (JSONObject) accountList.get(1);
        String accountId = Long.toString((long)account.get("id"));
        System.out.println(account);
        System.out.println(account.get("id"));
        
        
        
 //Parsing through each account in Yodlee to retrieve transactions and compute investment amount
        int counter = 0; //use for tracking number of transactions;
        for(int j = 0; j<accountList.size(); j++) {
        		JSONObject acc = (JSONObject) accountList.get(j);
        		System.out.println(acc.get("id"));
        		String accId = Long.toString((long)acc.get("id"));
        
        
        		String current_time_str = String.format("%tF", current_time);
	        String transactions = getTransactions(cobSession, userSession, accId, current_time_str);  
	        
	        System.out.println("transactions: " + transactions);
	        
	        JSONParser parser2 = new JSONParser();
			JSONObject json = (JSONObject) parser2.parse(transactions);
			JSONArray transactions_list = (JSONArray) json.get("transaction");
			System.out.println("Number of transactions: " + transactions_list.size());
			
		
		
		
		
			for (int i = 0; i < transactions_list.size(); i++) {
				Double investment = 0.00;
				JSONObject amounts = (JSONObject) transactions_list.get(i);
				JSONObject amounts2 = (JSONObject) amounts.get("amount");
				System.out.println("Transaction " + i + " : " + amounts2.get("amount") + " Transaction type:" + amounts.get("baseType"));
				//System.out.println(amounts.get("baseType"));
				if (amounts.get("baseType").equals("DEBIT")) {
					investment = margin - ((Double.valueOf(amounts2.get("amount").toString()))%margin);
					if (investment.equals(margin)) {
						investment = 0.00;
						counter = counter -1;
					}
					counter = counter + 1;
					System.out.println("investment amount: " + investment);
					investment_total = investment + investment_total;
					System.out.println("total investment: " + investment_total);
				}
	        }
        }
		//Make sure that the value is in 2 decimal places
		investment_total = investment_total * 100;
		investment_total = (double) (investment_total);
		investment_total = investment_total / 100;
		return investment_total; 
		
    }
    	
    	
    
    

}
