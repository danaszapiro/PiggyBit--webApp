package com.piggybit.demo;

import com.coinbase.authenticatedAPIcalls.Accounts;
import com.coinbase.authenticatedAPIcalls.Buys;
import com.coinbase.services.TokenExtractor;
import com.mongodb.MongoClient;
import com.piggybit.models.EventLog;
import com.piggybit.models.SettingsForm;
import com.piggybit.models.User;
import com.piggybit.mongoDB.UserRepository;
import com.piggybit.mongoDB.UserService;
import com.yodlee.beans.CobrandContext;
import com.yodlee.parser.GSONParser;
import com.yodlee.utils.GetTransactions;
import com.yodlee.utils.HTTP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Days;
import org.joda.time.LocalDate;
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

		User userSession = userController.getByUserName(user.getUserName());
		me = userSession;
		model.addAttribute("user", userSession);
		if(userSession != null && userSession.getCurrency() !=null){
			String currency = userSession.getCurrency();
			String crypto = userSession.getCryptocurrency();
			double priceMargin = userSession.getPriceMargin();
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
		if (result.hasErrors()) {
			return "settingsForm";
		}
		String currency = setForm.getCurrency();
		String crypto = setForm.getCrypto();
		double priceMargin = setForm.getPriceMargin();
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
		return homePage(settingsForm, me);
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model, User user) throws IOException, ParseException {
		model.addAttribute("user", new User());
		return "Register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerSuccessful(@ModelAttribute("user") User user, BindingResult result, Model model)
			throws IOException, ParseException {
		if (result.hasErrors()) {
			return "Register";
		}
		me = user;
		
		String code = me.getAuthCode();
		String fullToken = TokenExtractor.getToken(code);
		String accessToken = TokenExtractor.getAccessToken(fullToken);
		String refreshToken = TokenExtractor.getRefreshToken(fullToken);
		String coinbaseAcc = Accounts.getAccounts(Accounts.getAccountInfo(accessToken));
		me.setAccessToken(accessToken);
		me.setRefreshToken(refreshToken);
		me.setCoinbaseAccount(coinbaseAcc);
		userController.insert(me);
		log.info("Inserted : " + me);
		
		return userLogin(model);
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homePage(Model model, User user) throws IOException, ParseException {
		User userSession = userController.getByUserName(user.getUserName());
		me = userSession;
		
		model.addAttribute("user", me);

		// Get cobrandSession /////////////////////////
		GetTransactions getTrans = new GetTransactions();

		final String requestBody = "{" 
		+ "\"cobrand\":{"
		+ "\"cobrandLogin\":\"" + getTrans.cobrandLogin + "\""+ "," 
		+ "\"cobrandPassword\": " + "\"" + getTrans.cobrandPassword + "\"" + "," 
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

		//Get user session using cobsession and cobrand login and password  //////////////////////////
		String yodleeUsersession = getTrans.userLogin(cobSession, me.getYodleeUser(), me.getYodleePass());
		System.out.println("usersession: " + yodleeUsersession);

		String userAccounts = getTrans.getUserAccounts(cobSession, yodleeUsersession);
		System.out.println("userAccounts: " + userAccounts);
		
		String old_date = "2017-03-01";

		LocalDate now = LocalDate.now();
		LocalDate lastInvestmentPeriod = now.minusDays(900);
		System.out.println(now);
		System.out.println(lastInvestmentPeriod);

		 //Get investment amount //////////////////////////////////////////////////////
		//lastInvestmentPeriod.toString()
		Double investment_amount = getTrans.getInvestment(userAccounts, cobSession, yodleeUsersession, 
		me.getLastInvestmentDate().toString(), me.getPriceMargin());
		System.out.println(investment_amount);

		me.setSavedUpMoney(investment_amount);
		userController.update(me);
		
		LocalDate currentDate = new LocalDate();
		LocalDate lastDate = me.getLastInvestmentDate();
		int daysBetween = Days.daysBetween(lastDate , currentDate ).getDays();
		String refreshToken = me.getRefreshToken(); 
		String fullToken = TokenExtractor.refreshTheToken(refreshToken);
		String accessToken = TokenExtractor.getAccessToken(fullToken);
		refreshToken = TokenExtractor.getRefreshToken(fullToken);
		me.setAccessToken(accessToken);
		me.setRefreshToken(refreshToken);
		double amount = me.getSavedUpMoney();
		String crypto = me.getCryptocurrency();
		String accountId = me.getCoinbaseAccount();
		String currency = me.getCurrency();
		int period = me.getInvestmentPeriod();
		if(daysBetween >= me.getInvestmentPeriod() ) {
			System.out.println(Buys.makeABuy(accessToken, amount, crypto, accountId));
			me.setSavedUpMoney(0);
			me.setLastInvestmentDate(currentDate);
		}
		userController.update(me);
		model.addAttribute("user", me);
		return "home";
	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String homePage(@ModelAttribute("user") User user, BindingResult result, Model model) throws IOException, ParseException {
		return getSettings(model,me);
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
			    me = found;
				return homePage(model, me);
			}
		}
		return "Login";
	}
}
