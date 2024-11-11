package com.example.userCRUD.user;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.UUID;

@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_ID", nullable = false, unique = true)
    private UUID id;
    @Column(name = "first_name", nullable = false)

    private String firstname;
    @Column(name = "last_name", nullable = false)

    private String lastname;
    @Column(name = "age", nullable = false)

    private int age;
    @Column(name = "hobbies", nullable = false)

    private Array hobbies;
    @Column(name = "smoker", nullable = false)

    private Boolean smoker = false;
    @Column(name = "fav_food", nullable = false)
    private String fav_food;
}
