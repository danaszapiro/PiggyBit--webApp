package com.piggybit.mongoDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piggybit.models.User;

@Service
public class UserService 
{
  @Autowired
  private UserRepository userRepository;

  public User insert(User u)
  {
    u.setId(null);
    return userRepository.save(u);
  }
} 