package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUserName(String username);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
