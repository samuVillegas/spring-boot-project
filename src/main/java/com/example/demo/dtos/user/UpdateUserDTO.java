package com.example.demo.dtos.user;

import jakarta.validation.constraints.Size;

public class UpdateUserDTO {

    @Size(max = 50)
    private String name;
    @Size(max = 1000)
    private String address;
    @Size(max = 255)
    private String roleName;

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
