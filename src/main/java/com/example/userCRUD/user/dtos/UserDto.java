package com.example.userCRUD.user.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    @Schema(example = "Fritzli", description = "first or given name of this user")
    private String firstname;

    private String lastname;

    private int age;

    private List<String> hobbies;

    private boolean smoker;

    private String favoriteFood;
}
