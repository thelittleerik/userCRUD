package com.example.userCRUD.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@Tag(name = "User", description = "User specific endpoints for CRUD operations")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Retrieves all users from the database.",
            description = "A non-paginated response retrieving all user objects from the database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users have been fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Deletes a user from the database.",
            description = "Deletes a user by the given ID from the database. Returns status 200 if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Creates a new user.",
            description = "Creates a new user in the database with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid user details", content = @Content)
    })
    @PostMapping("/new")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.status(201).body(user);
    }

    @Operation(
            summary = "Updates an existing user.",
            description = "Updates the user details for the given user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}/edit")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOpt.get();

        if (user.getFirstname() != null) {
            existingUser.setFirstname(user.getFirstname());
        }
        if (user.getLastname() != null) {
            existingUser.setLastname(user.getLastname());
        }
        if (user.getAge() != 0) {
            existingUser.setAge(user.getAge());
        }
        if (user.getHobbies() != null) {
            existingUser.setHobbies(user.getHobbies());
        }
        existingUser.setSmoker(user.isSmoker());

        if (user.getFavoriteFood() != null) {
            existingUser.setFavoriteFood(user.getFavoriteFood());
        }

        userRepository.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }
}