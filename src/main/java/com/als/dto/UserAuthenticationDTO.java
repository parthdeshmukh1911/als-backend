package com.als.dto;



import lombok.Data;

@Data
public class UserAuthenticationDTO {
    private Long userId;
    private String username;
    private String password;
    private String role;

    // Constructors, getters, and setters
}
