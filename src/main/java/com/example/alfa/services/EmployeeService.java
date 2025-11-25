package com.example.alfa.services;

import com.example.alfa.exceptions.NoDataFoundException;
import com.example.alfa.exceptions.UserNotFoundException;
import com.example.alfa.models.Employee;
import com.example.alfa.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmployeeService  {

    @Autowired
    EmployeeRepository employeeRepository;

     public List<Employee> findAll() {
        var employees = (List<Employee>) employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new NoDataFoundException();
        }
        return employees;
    }

     public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }








}
