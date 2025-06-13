package com.example.userservice.service;

import com.example.userservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;
    private final String serviceDbApiUrl;

    public UserServiceImpl(RestTemplate restTemplate, @Value("${servicedb.api.url}") String serviceDbApiUrl) {
        this.restTemplate = restTemplate;
        this.serviceDbApiUrl = serviceDbApiUrl;
    }

    @Override
    public List<User> getAllUsers() {
        User[] users = restTemplate.getForObject(serviceDbApiUrl + "/users", User[].class);
        return Arrays.asList(users != null ? users : new User[0]);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try {
            User user = restTemplate.getForObject(serviceDbApiUrl + "/users/" + id, User.class);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public User createUser(User user) {
        return restTemplate.postForObject(serviceDbApiUrl + "/users", user, User.class);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        restTemplate.put(serviceDbApiUrl + "/users/" + id, userDetails);
        return getUserById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        restTemplate.delete(serviceDbApiUrl + "/users/" + id);
    }
}