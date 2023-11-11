package com.example.demo.dtos.role;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateRoleDTO {
    @Size(max = 255)
    @NotNull
    private String name;

    public String getName() {
        return name;
    }
}
