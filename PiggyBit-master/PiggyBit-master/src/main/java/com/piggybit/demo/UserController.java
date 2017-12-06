package com.piggybit.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.piggybit.models.User;
import com.piggybit.mongoDB.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAll() {
        List<User> users = this.userRepository.findAll();
        return users;
    }

    @PutMapping
    public void insert(@RequestBody User user) {
        this.userRepository.insert(user);
    }

    @PostMapping
    public void update(@RequestBody User user) {
        this.userRepository.save(user);
    }

    public void delete(@PathVariable("id") String id) {
        this.userRepository.delete(id);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") String id) {
        User user = this.userRepository.findById(id);
        return user;
    }

    @GetMapping("/{userName}")
    public User getByUserName(@PathVariable("userName") String userName) {
        User user = this.userRepository.findByUserName(userName);
        return user;
    }

}

