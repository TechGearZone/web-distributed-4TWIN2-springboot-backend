package com.techgear.services.user.service;

import com.techgear.services.user.dto.UserRegistrationDto;
import com.techgear.services.user.dto.UserResponseDto;
import java.util.List;

public interface IUserService {
    UserResponseDto registerUser(UserRegistrationDto registrationDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRegistrationDto userDto);
    void deleteUser(Long id);
    UserResponseDto getUserByEmail(String email);
    boolean existsByEmail(String email);
} 