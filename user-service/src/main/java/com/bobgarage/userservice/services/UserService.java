package com.bobgarage.userservice.services;

import com.bobgarage.userservice.dtos.RegisterUserRequest;
import com.bobgarage.userservice.dtos.UpdateUserRequest;
import com.bobgarage.userservice.dtos.UserDto;
import com.bobgarage.userservice.entities.Role;
import com.bobgarage.userservice.exceptions.DuplicateUserException;
import com.bobgarage.userservice.exceptions.UserNotFoundException;
import com.bobgarage.userservice.mappers.UserMapper;
import com.bobgarage.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public Iterable<UserDto> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(UUID userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
