package com.example.userCRUD.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.userCRUD.user.dtos.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "200", description = "All users have been fetched successfully")
            // the rest is redundant...
    })
    @GetMapping("/users")
    public List<User> getAllUsers() { // no need for ResponseEntity<> wrappers
        return userRepository.findAll();
    }

    @Operation(
            summary = "Finds users by their first name",
            description = "Retrieves a list of Users based on their first name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The search query has been successfully executed.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    array = @ArraySchema(schema = @Schema(implementation = User.class))
            ))

    })
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> findUserByName(@RequestParam("firstname") String name) {
         return ResponseEntity.ok(userRepository.findAllByFirstnameContaining((name)));
    }

    @Operation(
            summary= "Get user by their Id.",
            description = "Get a user by their given Id (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description ="The user is found and the their data is retrieved.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
                    schema = @Schema(implementation = UserDto.class)
            )),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid user details", content = @Content)
    })
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserDto userDto) {
        User regUser = new User();
        regUser.setFirstname(userDto.getFirstname());
        regUser.setLastname(userDto.getLastname());
        regUser.setAge(userDto.getAge());
        regUser.setHobbies(userDto.getHobbies());
        regUser.setSmoker(userDto.isSmoker());
        regUser.setFavoriteFood(userDto.getFavoriteFood());
        userRepository.save(regUser);
    }

    @Operation(
            summary = "Updates an existing user.",
            description = "Updates the user details for the given user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
            )),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}/edit")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isEmpty()) {
            // alternatively: throw an Exception annotated with @ResponseCode.
            // it gets more readable than the ResponseEntity<> wrappers.
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOpt.get();

        if (userDto.getFirstname() != null) {
            existingUser.setFirstname(userDto.getFirstname());
        }
        if (userDto.getLastname() != null) {
            existingUser.setLastname(userDto.getLastname());
        }
        if (userDto.getAge() != 0) {
            existingUser.setAge(userDto.getAge());
        }
        if (userDto.getHobbies() != null) {
            existingUser.setHobbies(userDto.getHobbies());
        }
        existingUser.setSmoker(userDto.isSmoker());

        if (userDto.getFavoriteFood() != null) {
            existingUser.setFavoriteFood(userDto.getFavoriteFood());
        }

        // there is no need to save. the existingUser is a managed entity anyway so the transaction will be committed
        // at the end of this method anyway. this is a common misconception I see often.
        return ResponseEntity.ok(existingUser);
    }
}
