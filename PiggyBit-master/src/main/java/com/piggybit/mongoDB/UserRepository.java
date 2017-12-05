package com.piggybit.mongoDB;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.piggybit.models.User;
import com.piggybit.demo.UserController;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findById(String id);

	User findByUserName(String userName);

	List<User> findByFirstName(String firstName);
}
