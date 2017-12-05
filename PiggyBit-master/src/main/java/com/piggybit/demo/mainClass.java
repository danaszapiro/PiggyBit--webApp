package com.piggybit.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class mainClass implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(mainClass.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {

		User user1 = new User("DanaSzapiro", "password", "Dana", "Szapiro", "danasz@bu.edu");
		User user2 = new User("IvanWong", "Password2", "Ivan", "Wong", "Ivan@bu.edu");

		List<User> users = Arrays.asList(user1, user2);
		userRepository.deleteAll();
		userRepository.save(users);
	}
	/*
	 * public static void main(String[] args) throws IOException, ParseException {
	 * System.out.println(PriceFetcher.getRequest()); String price =
	 * PriceFetcher.processJSON(crypto, currency);
	 * System.out.println("Cryptocurrency requested: " + crypto);
	 * System.out.println("Price for " + crypto + " in " + currency + " is " +
	 * price); }
	 */

}
