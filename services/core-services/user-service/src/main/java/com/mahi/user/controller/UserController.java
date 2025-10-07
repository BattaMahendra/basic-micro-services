package com.mahi.user.controller;

import com.mahi.user.dto.UserRequestDto;
import com.mahi.user.dto.UserResponseDto;
import com.mahi.user.entity.User;
import com.mahi.user.mapper.UserMapper;
import com.mahi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated  // enables validation for @PathVariable and @RequestParam
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Create user
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        String correlationId = UUID.randomUUID().toString();
        log.info("[{}] Creating new user: {}", correlationId, userRequestDto.getEmail());

        User user = userMapper.toEntity(userRequestDto);
        User savedUser = userService.createUser(user);
        UserResponseDto responseDto = userMapper.toDto(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponseDto> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Updating user with ID: {}", id);
        User updatedUser = userService.updateUser(id, userMapper.toEntity(userRequestDto));
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
