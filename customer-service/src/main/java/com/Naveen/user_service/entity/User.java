package com.Naveen.user_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public @Data class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_Name")
    private  String lastName;

    @Column(name = "email")
    private String email;
}
