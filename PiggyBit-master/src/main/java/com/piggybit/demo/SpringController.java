package com.piggybit.demo;

import com.coinbase.authenticatedAPIcalls.Accounts;
import com.coinbase.services.TokenExtractor;
import com.mongodb.MongoClient;
import com.piggybit.models.SettingsForm;
import com.piggybit.models.User;
import com.piggybit.mongoDB.UserRepository;
import com.piggybit.mongoDB.UserService;

import com.yodlee.utils.*;

import com.yodlee.beans.CobrandContext;
import com.yodlee.parser.GSONParser;
import com.yodlee.beans.UserContext;
import com.yodlee.utils.HTTP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Controller
@Configuration
public class SpringController extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserController userController;

	private User me;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/").permitAll();
	}

	private static final Log log = LogFactory.getLog(SpringController.class);

	@GetMapping("/user/me")
	public Principal user(Principal principal) {
		return principal;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String getSettings(Model model, User user) {

		User userSession = userController.getByUserName(user.getUserName());
		me = userSession;
		model.addAttribute("user", userSession);
		if(userSession != null && userSession.getCurrency() !=null){
			String currency = userSession.getCurrency();
			String crypto = userSession.getCryptocurrency();
			int priceMargin = userSession.getPriceMargin();
			int recurringPeriod = userSession.getInvestmentPeriod();
			SettingsForm setForm = new SettingsForm(currency, crypto, priceMargin, recurringPeriod);
			model.addAttribute("settingsForm",setForm);
			return "settingsForm";
		}
		else {
			model.addAttribute("settingsForm", new SettingsForm());
			return "settingsForm";
		}
	}

	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public String settingsResult(@ModelAttribute("setForm") SettingsForm setForm, BindingResult result,
								 Model settingsForm) throws IOException, ParseException {
		System.out.println("settings post");
		if (result.hasErrors()) {
			return "settingsForm";
		}
		String currency = setForm.getCurrency();
		String crypto = setForm.getCrypto();
		int priceMargin = setForm.getPriceMargin();
		int recurringPeriod = setForm.getRecurringPeriod();

		settingsForm.addAttribute("currency", currency);
		settingsForm.addAttribute("crypto", crypto);
		settingsForm.addAttribute("priceMargin", priceMargin);
		settingsForm.addAttribute("recurringPeriod", recurringPeriod);

		/*int period = Integer.valueOf(recurringPeriod);
		int price = Integer.valueOf(priceMargin);*/

		me.setCurrency(currency);
		me.setCryptocurrency(crypto);
		me.setPriceMargin(priceMargin);
		me.setInvestmentPeriod(recurringPeriod);

		userController.update(me);
		return "settingsConfirmed";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model, User user) throws IOException, ParseException {
		model.addAttribute("user", new User());
		
		//
		String cobrandLogin = "sbCobd74e196a9cfac3ff5c74d3ee313bf2de6a";
	    String cobrandPassword = "1734ef64-b099-492c-8797-b0b00c40a495";
		
		GetTransactions getTrans = new GetTransactions();
		
		final String requestBody = "{" 
				+ "\"cobrand\":{"
				+ "\"cobrandLogin\":\"" + cobrandLogin + "\""+ "," 
				+ "\"cobrandPassword\": " + "\"" + cobrandPassword + "\"" + "," 
				+ "\"locale\": \"en_US\"" 
				+ "}" 
			  + "}";
        
        String coBrandLoginURL = "https://developer.api.yodlee.com/ysl/restserver/" + "v1/cobrand/login";
        String cobrandjsonResponse = HTTP.doPost(coBrandLoginURL, requestBody);
        System.out.println("CobrandJSONResponse");
        System.out.println(cobrandjsonResponse);
        
       CobrandContext coBrand = (CobrandContext) GSONParser.handleJson(
				cobrandjsonResponse, com.yodlee.beans.CobrandContext.class);
        
        String cobSession = coBrand.getSession().getCobSession();
        
		
        System.out.println("cobSession: " + cobSession);

//Get user session using cobsession and cobrand login and password
        String userSession = getTrans.userLogin(cobSession, "sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a3", "sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a3#123");
        
        System.out.println("usersession: " + userSession);
		
		//
        String userAccounts = getTrans.getUserAccounts(cobSession, userSession);
        System.out.println("userAccounts: " + userAccounts);
        
        Date time1 = new Date();
        Date time2 = new Date();
        Double investment_amount = getTrans.getInvestment(userAccounts, cobSession, userSession, time1, time2, 5);
        System.out.println(investment_amount);
		
        
		return "Register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerSuccessful(@ModelAttribute("user") User user, BindingResult result, Model model)
			throws IOException, ParseException {
		if (result.hasErrors()) {
			return "Register";
		}

		me = user;
		userController.insert(me);
		
		me.setYodleeUser("sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a1");
		me.setYodleePass("sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a1#123");

		
		log.info("Inserted : " + user);
		return userLogin(model);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String userLogin(Model model) {

		model.addAttribute("user", new User());
		return "Login";
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login( @ModelAttribute("user") User user,
						BindingResult result, Model model) throws IOException, ParseException {

		String userName = user.getUserName().toString();
		String password = user.getPassword().toString();
		User found = userController.getByUserName(userName);
		if( found != null){
			if (found.getPassword() != null && password.equals(found.getPassword().toString())){
				System.out.println(found.getPassword());
			    me = found;
				return getSettings(model, me);
			}
		}
		return "Login";
	}
}
