package com.ashik619.testproject.controller;

import com.ashik619.testproject.models.User;
import com.ashik619.testproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/test")
    @ResponseBody
    String hello() {
        return "Hello World!";
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/create")
    public User createUser(@Valid @RequestBody User note) {
        return userRepository.save(note);
    }
    
}