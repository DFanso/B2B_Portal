package com.B2B.Portal.user.dto;

import jakarta.validation.constraints.*;

public class UserDTO {

    private String userId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email does not match the required format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Type is mandatory")
    @Pattern(regexp = "^(CUSTOMER|SUPPLIER|ADMIN)$", message = "Type should be either ADMIN, CUSTOMER, or SUPPLIER")
    private String type;




    public UserDTO() {
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }
}