package com.bobgarage.userservice.controllers;

import com.bobgarage.userservice.dtos.UpdateUserRequest;
import com.bobgarage.userservice.dtos.UserDto;
import com.bobgarage.userservice.services.UserService;
import com.bobgarage.userservice.exceptions.DuplicateUserException;
import com.bobgarage.userservice.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable(name = "id") UUID id,
            @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
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
