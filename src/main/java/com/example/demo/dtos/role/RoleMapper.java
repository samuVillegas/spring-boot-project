package com.example.demo.dtos.role;

import com.example.demo.models.Role;

public class RoleMapper {
    public static Role createRoleDTOToRole(CreateRoleDTO createRoleDTO){
      Role role = new Role();
      role.setName(createRoleDTO.getName());
      return role;
    };
}
