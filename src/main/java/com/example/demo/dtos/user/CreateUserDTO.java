package com.example.demo.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserDTO {
    @Email
    @Size(max = 255)
    @NotNull
    private String email;
    @Size(max = 60)
    @NotNull
    private String password;
    @Size(max = 50)
    @NotNull
    private String name;
    @Size(max = 1000)
    @NotNull
    private String address;

    @Size(max = 255)
    @NotNull
    private String roleName;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRoleName() {
        return roleName;
    }
}
