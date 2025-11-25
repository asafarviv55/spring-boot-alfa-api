package com.example.alfa.controllers;


import com.example.alfa.exceptions.LoginFailedException;
import com.example.alfa.models.User;
import com.example.alfa.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/alfa")
public class Controller {


    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    UserService userService;


    @GetMapping("register")
    public void register(@RequestParam String username, @RequestParam String password) {
        logger.info("Register enter.. username: " + username + " password: " +password);
        userService.register(username,password);
    }

    @GetMapping(path = "login" )
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) {
        logger.info("login enter.. username: " + username + " password: " +password);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        User user = userService.login(username,password);
        if( user == null)
            throw new LoginFailedException();
        return ResponseEntity.ok(user);
    }

    @GetMapping("user/{id}")
    public void getUserById(@PathVariable  Long id) {
        logger.info("getUserById enter.. : ");
        userService.findById(id);
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        logger.info("getUserById enter.. : ");
        return userService.findAll();
    }





}
