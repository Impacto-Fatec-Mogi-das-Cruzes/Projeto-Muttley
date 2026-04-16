package com.project.muttley.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.muttley.model.User;
import com.project.muttley.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired UserRepository userRepository;

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
