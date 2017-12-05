package com.piggybit.demo;

import com.coinbase.authenticatedAPIcalls.Accounts;
import com.coinbase.services.TokenExtractor;
import com.mongodb.MongoClient;
import com.piggybit.models.SettingsForm;
import com.piggybit.models.User;
import com.piggybit.mongoDB.UserRepository;
import com.piggybit.mongoDB.UserService;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

import java.io.IOException;
import java.security.Principal;

@Controller
public class SpringController extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;
	
	private static final Log log = LogFactory.getLog(SpringController.class);
	//MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "database"));
	//private UserController userController = new UserController();

    @GetMapping("/user/me")
    public Principal user(Principal principal) {
        return principal;
    }
    
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String getSettings(Model model) {
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

		return "settingsConfirmed";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(User user) throws IOException, ParseException {

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
		userRepository.save(user);
		log.info("Inserted : " + user);
		return "Login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute("user") User user) throws IOException, ParseException {

		return "Login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("settingsForm") SettingsForm settingsForm, @ModelAttribute("user") User user,
			BindingResult result, Model model) throws IOException, ParseException {

		User found = userRepository.findByUserName(user.getUserName());
		System.out.println(user.getUserName());
		System.out.println(found.getUserName());
		if (found == null || user.getUserName() == null) {
			return "LoginFail";
		} else if (found.getPassword() == user.getPassword()) {
			return "settingsForm";
		}

		return "LoginFail";
	}
}
/*
 * -----------------------------------------(not sure if we need these) DB REST
 * CALLS------------------------------------------------
 */

/*
 * @RequestMapping("/persons")
 * 
 * @Slf4j
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')") class PersonController {
 * 
 * @Autowired private UserRepository repository;
 * 
 * @PreAuthorize("hasRole('ROLE_USER')")
 * 
 * @RequestMapping(value = "/{id}", method = RequestMethod.GET) User
 * getUserById(@PathVariable String id) {
 * log.info("getUserById with parameter $id"); return repository.findOne(id); }
 * 
 * @PreAuthorize("hasRole('ROLE_USER')")
 * 
 * @RequestMapping(method = RequestMethod.GET) List<User> getAllUsers() {
 * log.info("getAllUsers()"); return repository.findAll(); }
 * 
 * @RequestMapping(method = RequestMethod.POST) User createUser(@RequestBody
 * User newUser) { log.info("createUser with parameter $newUser"); newUser.id =
 * null; return repository.save(newUser); }
 * 
 * @RequestMapping(value = "/{id}", method = RequestMethod.PUT) void
 * updateUser(@PathVariable String id, @RequestBody User updatedUser) {
 * log.info("updateUser with parameter $id and $updatedUser"); updatedUser.id =
 * id; repository.save(updatedUser); }
 * 
 * @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) void
 * removeUser(@PathVariable String id) {
 * log.info("removeUser with parameter $id"); repository.delete(id); }
 * 
 * @PreAuthorize("hasRole('ROLE_USER')")
 * 
 * @RequestMapping(value = "/search/byFirstName/{firstName}", method =
 * RequestMethod.GET) List<User> getUserByFirstName(@PathVariable String
 * firstName) { log.info("getUserByFirstName with parameter $firstName"); return
 * repository.findByFirstName(firstName); } }
 */
