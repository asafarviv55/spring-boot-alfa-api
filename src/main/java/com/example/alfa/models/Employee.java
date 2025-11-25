package com.example.alfa.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="name")
    private String name;


    @Column(name="is_active")
    private String isActive;


    @Column(name="address_street")
    private String addressStreet;


    @Column(name="address_number")
    private String addressNumber;

    @Column(name="address_city")
    private String addressCity;

    @Column(name="address_country")
    private String address_country;


}
