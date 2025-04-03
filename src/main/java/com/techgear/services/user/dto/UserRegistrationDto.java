package com.techgear.services.user.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String address;
} 