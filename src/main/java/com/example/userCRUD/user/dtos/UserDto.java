package com.example.userCRUD.user.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {

    private String firstname;

    private String lastname;

    private int age;
    private List<String> hobbies;

    private boolean smoker;

    private String favoriteFood;
}
