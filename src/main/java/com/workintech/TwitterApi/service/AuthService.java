package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.LoginRequest;
import com.workintech.TwitterApi.dto.RegisterRequest;
import com.workintech.TwitterApi.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
}