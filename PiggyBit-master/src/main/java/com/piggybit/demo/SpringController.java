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
import com.yodlee.utils.HTTP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

		model.addAttribute("settingsForm", new SettingsForm());
		return "settingsForm";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public String settingsResult(@ModelAttribute("setForm") SettingsForm setForm, BindingResult result,
								 Model settingsForm) throws IOException, ParseException {
		if (result.hasErrors()) {
			return "settingsForm";
		}
		String currency = setForm.getCurrency();
		String crypto = setForm.getCrypto();
		String priceMargin = setForm.getPriceMargin();
		String recurringPeriod = setForm.getRecurringPeriod();

		settingsForm.addAttribute("currency", currency);
		settingsForm.addAttribute("crypto", crypto);
		settingsForm.addAttribute("priceMargin", priceMargin);
		settingsForm.addAttribute("recurringPeriod", recurringPeriod);

		/*int period = Integer.valueOf(recurringPeriod);
		int price = Integer.valueOf(priceMargin);

		me.setCurrency(currency);
		me.setCryptocurrency(crypto);
		me.setPriceMargin(price);
		me.setInvestmentPeriod(period);

		userController.update(me);
		userRepository.save(me);*/

		return "settingsConfirmed";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model, User user) throws IOException, ParseException {

		return "Register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerSuccessful(@ModelAttribute("user") User user, BindingResult result, Model model)
			throws IOException, ParseException {
		if (result.hasErrors()) {
			return "Register";
		}

		String auth = user.getAuthCode();
		String token = TokenExtractor.getToken(auth);
		String accessToken = TokenExtractor.getAccessToken(token);
		String refreshToken = TokenExtractor.getRefreshToken(token);
		String coinbaseAcc = Accounts.getAccounts(Accounts.getAccountInfo(accessToken));
		
		user.setAccessToken(accessToken);
		user.setRefreshToken(refreshToken);
		user.setAuthCode(auth);
		user.setCoinbaseAccount(coinbaseAcc);
		// repository.save(user);
		//mongoOps.insert(user);

		me = user;
		me.setCurrency("USD");
		me.setCryptocurrency("BTN");
		me.setPriceMargin(1);
		me.setInvestmentPeriod(1);
		
		me.setYodleeUser("sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a1");
		me.setYodleePass("sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a1#123");
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
        System.out.println(cobrandjsonResponse);
        
        CobrandContext coBrand = (CobrandContext) GSONParser.handleJson(
				cobrandjsonResponse, com.yodlee.beans.CobrandContext.class);
        
        String cobSession = coBrand.getSession().getCobSession();
        
        System.out.println("cobSesssion: " + cobSession);

//Get user session using cobsession and cobrand login and password
        String userSession = getTrans.userLogin(cobSession, "sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a3", "sbMemd74e196a9cfac3ff5c74d3ee313bf2de6a3#123");
        
        System.out.println("usersession: " + userSession);
		
		userController.insert(me);
		log.info("Inserted : " + user);
		return "Login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String userLogin(Model model) {

		model.addAttribute("user", new User());
		return "Login";
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login( @ModelAttribute("user") User user,
						BindingResult result, Model model) throws IOException, ParseException {

		System.out.println("Login POST page");


		String userName = user.getUserName().toString();
		String password = user.getPassword().toString();
		User found = userController.getByUserName(userName);
		if( found != null){
			System.out.println(found.getUserName());
			if (found.getPassword() != null && password.equals(found.getPassword().toString())){
				System.out.println(found.getPassword());
			    me = found;
				return getSettings(model, me);
			}
		}
		return "Login";
	}
}
