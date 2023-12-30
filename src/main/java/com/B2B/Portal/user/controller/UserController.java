package com.B2B.Portal.user.controller;

import com.B2B.Portal.product.dto.ProductDTO;
import com.B2B.Portal.user.dto.UserDTO;
import com.B2B.Portal.user.dto.UserResponseDTO;
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

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestParam String email, @RequestParam String password) {
        UserResponseDTO userResponseDTO = userService.authenticateUser(email, password);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User userEntity = modelMapper.map(userDTO, User.class);
        User createdUser = userService.createUser(userEntity);
        UserDTO createdUserDTO = modelMapper.map(createdUser, UserDTO.class);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate-supplier")
    public ResponseEntity<?> isValidSupplier(@RequestParam String userId) {
        //Long ID = Long.parseLong(userId);
        boolean isValid = userService.isValidSupplier(userId);
        return ResponseEntity.ok(isValid);
    }
}
