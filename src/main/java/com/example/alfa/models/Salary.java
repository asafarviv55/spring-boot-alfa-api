package com.example.alfa.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="salaries")
public class Salary {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
    private Long id;


    @Column(name = "month")
    private String month;

    @Column(name = "gross")
    private String gross;

    @Column(name = "tax")
    private String tax;

    @Column(name = "total")
    private String total;


}
