package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String keycloakId, UserDTO userDTO);
    UserDTO getUserByKeycloakId(String keycloakId);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    void deleteUser(String keycloakId);
    boolean existsByEmail(String email);
} 