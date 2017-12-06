package com.piggybit.demo;

import com.coinbase.authenticatedAPIcalls.Accounts;
import com.coinbase.services.TokenExtractor;
import com.mongodb.MongoClient;
import com.piggybit.models.SettingsForm;
import com.piggybit.models.User;
import com.piggybit.mongoDB.UserRepository;
import com.piggybit.mongoDB.UserService;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;


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
	

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String home(Model model) throws IOException, ParseException {
		return "home";
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
	
/*
 * Logout Functionality 
 */
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	}
		return "Login";
	}
}
