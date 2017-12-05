package com.piggybit;

import com.piggybit.mongoDB.dbSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.List;
import java.util.Arrays;

import com.piggybit.models.User;
import com.piggybit.mongoDB.UserRepository;

@SpringBootApplication
public class mainClass {

	/*@Autowired
	private UserRepository userRepository;

	public UserRepository getUserRepository(){
		return this.userRepository;
	}*/

	public static void main(String[] args) {
		SpringApplication.run(mainClass.class, args);
	}
	
	/*
	@Override
	public void run(String... strings) throws Exception {

		dbSeeder seeder = new dbSeeder(userRepository);
	}/*
	/*
	 * public static void main(String[] args) throws IOException, ParseException {
	 * System.out.println(PriceFetcher.getRequest()); String price =
	 * PriceFetcher.processJSON(crypto, currency);
	 * System.out.println("Cryptocurrency requested: " + crypto);
	 * System.out.println("Price for " + crypto + " in " + currency + " is " +
	 * price); }
	 */

}
