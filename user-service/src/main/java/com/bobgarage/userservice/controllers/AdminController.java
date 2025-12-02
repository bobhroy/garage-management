package com.bobgarage.userservice.controllers;

import com.bobgarage.userservice.dtos.RegisterUserRequest;
import com.bobgarage.userservice.dtos.UserDto;
import com.bobgarage.userservice.exceptions.DuplicateUserException;
import com.bobgarage.userservice.exceptions.UserNotFoundException;
import com.bobgarage.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) {

        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/admin/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUser() {
        return ResponseEntity.badRequest().body(
                Map.of("email", "Email is already registered.")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }
}
