package com.B2B.Portal.user.service;

import com.B2B.Portal.user.dto.UserDTO;
import com.B2B.Portal.user.exception.UserAlreadyExistsException;
import com.B2B.Portal.user.exception.UserNotFoundException;
import com.B2B.Portal.user.model.User;
import com.B2B.Portal.user.repository.UserRepository;
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



    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
            existingUser.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword());
        }
        if (userDetails.getType() != null && !userDetails.getType().isEmpty()) {
            existingUser.setType(userDetails.getType());
        }

        return userRepository.save(existingUser);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}