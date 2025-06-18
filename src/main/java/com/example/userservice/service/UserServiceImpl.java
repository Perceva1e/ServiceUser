package com.example.userservice.service;

import com.example.userservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Retrieving all users from external API");
        User[] users = restTemplate.getForObject("/users", User[].class);
        return Arrays.asList(users != null ? users : new User[0]);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Retrieving user with ID: {}", id);
        try {
            User user = restTemplate.getForObject("/users/" + id, User.class);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("Failed to retrieve user with ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public User createUser(User user) {
        log.info("Creating user with email: {}", user.getEmail());
        return restTemplate.postForObject("/users", user, User.class);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        log.info("Updating user with ID: {}", id);
        restTemplate.put("/users/" + id, userDetails);
        return getUserById(id).orElseThrow(() -> {
            log.error("User with ID {} not found after update", id);
            return new RuntimeException("User not found with id " + id);
        });
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        restTemplate.delete("/users/" + id);
    }
}