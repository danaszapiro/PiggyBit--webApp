package com.piggybit.mongoDB;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.piggybit.models.User;
import com.piggybit.demo.UserController;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	public User findById(String id);

	public User findByUserName(String userName);

	public List<User> findByFirstName(String firstName);
}
