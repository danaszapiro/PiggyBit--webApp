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

	public static void main(String[] args) {
		SpringApplication.run(mainClass.class, args);
	}

}
