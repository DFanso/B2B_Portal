package com.B2B.Portal.user.controller;

import com.B2B.Portal.user.dto.UserDTO;
import com.B2B.Portal.user.model.User;
import com.B2B.Portal.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User userEntity = modelMapper.map(userDTO, User.class);
        User createdUser = userService.createUser(userEntity);
        UserDTO createdUserDTO = modelMapper.map(createdUser, UserDTO.class);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
