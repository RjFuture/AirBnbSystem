package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.exceptions.ResourceNotFoundException;
import com.onlinebookingsystem.airbnbsystem.models.User;
import com.onlinebookingsystem.airbnbsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id :"+userId));
    }
}
