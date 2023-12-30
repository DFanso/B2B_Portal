package com.B2B.Portal.user.service;

import com.B2B.Portal.user.dto.UserDTO;
import com.B2B.Portal.user.dto.UserResponseDTO;
import com.B2B.Portal.user.exception.BadCredentialsException;
import com.B2B.Portal.user.exception.UserAlreadyExistsException;
import com.B2B.Portal.user.exception.UserNotFoundException;
import com.B2B.Portal.user.model.User;
import com.B2B.Portal.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper ;
    private final BCryptPasswordEncoder passwordEncoder;





    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponseDTO authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return modelMapper.map(user, UserResponseDTO.class);
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        if (userExists(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }



    public User updateUser(String id, @Valid UserDTO userDetailsDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userDetailsDTO.setUserId(id);
        // Update existingUser properties from userDetailsDTO
        modelMapper.map(userDetailsDTO, existingUser);

        return userRepository.save(existingUser);
    }


    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public boolean isValidSupplier(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return "SUPPLIER".equals(user.getType());
    }
}