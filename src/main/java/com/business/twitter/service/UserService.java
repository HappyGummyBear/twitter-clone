package com.business.twitter.service;

import com.business.twitter.dto.*;
import com.business.twitter.entity.User;
import com.business.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest createUserRequest) {
        User user = new User(
                createUserRequest.userName(),
                createUserRequest.firstName(),
                createUserRequest.lastName(),
                createUserRequest.email()
        );

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getUserName(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail()
        );
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        return new UserResponse(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public DeleteUserResponse deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found.");
        }

        userRepository.deleteById(userId);

        return new DeleteUserResponse(
                "User deleted successfully"
        );
    }
}
