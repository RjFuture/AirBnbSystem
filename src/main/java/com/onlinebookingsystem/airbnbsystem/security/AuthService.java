package com.onlinebookingsystem.airbnbsystem.security;


import com.onlinebookingsystem.airbnbsystem.dto.LoginDto;
import com.onlinebookingsystem.airbnbsystem.dto.SignUpRequestDto;
import com.onlinebookingsystem.airbnbsystem.dto.UserDto;
import com.onlinebookingsystem.airbnbsystem.models.User;
import com.onlinebookingsystem.airbnbsystem.models.enums.Role;
import com.onlinebookingsystem.airbnbsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto)
    {
        User user =  userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

        if(user != null)
        {
            throw new RuntimeException("User is already present with same email id");
        }

        User newUser = modelMapper.map(signUpRequestDto,User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

         user = userRepository.save(newUser);
         return modelMapper.map(user,UserDto.class);
    }

    public  String [] login(LoginDto loginDto)
    {
       Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail()
               ,loginDto.getPassword()));

       User user = (User) authentication.getPrincipal();

       String arr[] = new String[2];

       arr[0] = jwtService.generateAccessToken(user);
       arr[1] = jwtService.generateRefreshToken(user);

       return  arr;
    }
}
