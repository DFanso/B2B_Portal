package com.B2B.Portal.user.service;

import com.B2B.Portal.user.dto.UserDTO;
import com.B2B.Portal.user.exception.UserAlreadyExistsException;
import com.B2B.Portal.user.exception.UserNotFoundException;
import com.B2B.Portal.user.model.User;
import com.B2B.Portal.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper ;



    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (userExists(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        return userRepository.save(user);
    }



    public User updateUser(Long id, @Valid UserDTO userDetailsDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userDetailsDTO.setUserId(id);
        // Update existingUser properties from userDetailsDTO
        modelMapper.map(userDetailsDTO, existingUser);

        return userRepository.save(existingUser);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public boolean isValidSupplier(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return "SUPPLIER".equals(user.getType());
    }
}