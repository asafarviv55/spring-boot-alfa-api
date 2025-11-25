package com.example.alfa.services;

import com.example.alfa.models.User;

import java.util.List;

public interface IUserService {

    User findById(Long id);

    List<User> findAll();

}
