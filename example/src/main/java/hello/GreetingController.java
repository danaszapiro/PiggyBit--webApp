package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import beans.CobrandContext;
import beans.UserContext;
import parser.GSONParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.lang.Math;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.*;

import java.util.Iterator;

import util.HTTP;

@Controller
public class GreetingController {
	
	private String localURLVer1 = "https://developer.api.yodlee.com/ysl/restserver/";

    @GetMapping("/greeting")
    public String greetingForm(Model model) throws IOException, ParseException {
        model.addAttribute("greeting", new Greeting());
        String cobSession = "";
        String userSession = "";
        String userAccounts = "";
        String cobrandLogin = "sbCobd74e196a9cfac3ff5c74d3ee313bf2de6a";
        String cobrandPassword = "1734ef64-b099-492c-8797-b0b00c40a495";
        String accountId = "";
        String transactions = ""; 
        String date = "2013-12-01";
        double investment_total = 0.00;
        double margin = 10.00;
        
        final String requestBody = "{" 
				+ "\"cobrand\":{"
				+ "\"cobrandLogin\":\"" + cobrandLogin + "\""+ "," 
				+ "\"cobrandPassword\": " + "\"" + cobrandPassword + "\"" + "," 
				+ "\"locale\": \"en_US\"" 
				+ "}" 
			  + "}";
        
        String coBrandLoginURL = "https://developer.api.yodlee.com/ysl/restserver/" + "v1/cobrand/login";
        String cobrandjsonResponse = HTTP.doPost(coBrandLoginURL, requestBody);
        System.out.println(cobrandjsonResponse);
        
        CobrandContext coBrand = (CobrandContext) GSONParser.handleJson(
				cobrandjsonResponse, beans.CobrandContext.class);
        
        cobSession = coBrand.getSession().getCobSession();
        
        System.out.println("cobSesssion: " + cobSession);

        userSession = userLogin(cobSession, "sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a3", "sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a3#123");
        
        System.out.println("usersession: " + userSession);
        
        userAccounts = getUserAccounts(cobSession, userSession);
        
        System.out.println("userAccounts: " + userAccounts);
        
        JSONParser parser = new JSONParser();
        JSONObject userAccount = (JSONObject) parser.parse(userAccounts);
        JSONArray accountList = (JSONArray) userAccount.get("account");
        JSONObject account = (JSONObject) accountList.get(0);
        accountId = Long.toString((long)account.get("id"));
        System.out.println(account);
        System.out.println(account.get("id"));
        
        
        transactions = getTransactions(cobSession, userSession, accountId, date);  
        
        System.out.println("transactions: " + transactions);
        
        JSONParser parser2 = new JSONParser();
		JSONObject json = (JSONObject) parser2.parse(transactions);
		JSONArray transactions_list = (JSONArray) json.get("transaction");
		System.out.println("Number of transactions: " + transactions_list.size());
		
		
		for (int i = 0; i < transactions_list.size(); i++) {
			Double investment = 0.00;
			JSONObject amounts = (JSONObject) transactions_list.get(i);
			JSONObject amounts2 = (JSONObject) amounts.get("amount");
			System.out.println(amounts2.get("amount"));
			System.out.println(amounts.get("baseType"));
			if (amounts.get("baseType").equals("DEBIT")) {
				investment = margin - ((Double.valueOf(amounts2.get("amount").toString()))%margin);
				if (investment.equals(margin)) {
					investment = 0.00;
				}
				System.out.println("investment amount: " + investment);
				investment_total = investment + investment_total;
			}
        }
		
		//Make sure that the value is in 2 decimal places
		investment_total = investment_total * 100;
		investment_total = (double) ((int)investment_total);
		investment_total = investment_total / 100;
		
		
        System.out.println("Total investment: " + investment_total/1.00);
        return "greeting";
        
        
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greetings) {
        return "result";
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userSession = null;
		
		String userName = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");
		
		String cobSession = (String) request.getSession().getAttribute("cobSession");
		
		try {
			//Yodlee User Login Call (/user/login)
			userSession = userLogin(cobSession, userName, password);
			request.getSession().setAttribute("userSession", userSession);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(userSession != null) {
			
			sendAjaxResponse(response, "{'error':'false', 'message':'User authentication successfull'}");

			
		}else {
			response.setContentType("text/plain");  
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{'error':'true', 'message':'Error in user Login, Invalid user credentials.'}"); 
		}
		
	}
    
    private String userLogin(String cobSession, String userName, String password) {

		String userSession = null;

		try {
			Map<String, String> loginTokens = new HashMap<String, String>();
			loginTokens.put("cobSession", cobSession);

			// User login
			String userLoginURL = localURLVer1 + "v1/user/login";

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
					userjsonResponse, beans.UserContext.class);

			if (!userjsonResponse.contains("errorCode")) {
				userSession = member.getUser().getSession().getUserSession();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userSession;
	}
    
    private void sendAjaxResponse(HttpServletResponse response,
			String responseString) throws IOException {
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseString);  
		
	}
    
    private String getUserAccounts(String cobSession, String userSession) {
		String accountURL = localURLVer1 + "v1/accounts";
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
    
    private String getTransactions(String cobSession, String userSession,
			String accountId, String date) {
		
		String txnJson ="";
		//og
		//String TransactionUrl = localURLVer1 + "v1/transactions" + "?fromDate=2013-01-01&accountId="+accountId;
		String TransactionUrl = localURLVer1 + "v1/transactions" + "?fromDate=" + date + "&accountId="+accountId;

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
    
}