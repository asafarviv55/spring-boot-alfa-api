package com.example.alfa.services;


import com.example.alfa.exceptions.NoDataFoundException;
import com.example.alfa.exceptions.UserNotFoundException;
import com.example.alfa.models.Employee;
import com.example.alfa.models.User;
import com.example.alfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService implements IUserService {


    @Autowired
    UserRepository userRepository;
    public static HashMap<Integer,Employee> employeeCache = new HashMap<Integer, Employee>();


    public User login(String username, String password) {
        return userRepository.isUserExists(username,password);
    }

    public User register(String username,String password){
        if( ! isUserExists(username,password)  )
            saveUser(username,password);
        return null;
    }

    @Override
    public List<User> findAll() {
        var users = (List<User>) userRepository.findAll();
        if (users.isEmpty()) {
            throw new NoDataFoundException();
        }
        return users;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    private boolean isUserExists(String username, String password) {
        User user = userRepository.isUserExists(username,password);
        if(user != null)
            return true;
        return false;
    }

    private void saveUser(String username,String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }




}
