package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService{
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<Role> findAll(){
        return this.roleRepository.findAll();
    };

    public Role save(Role role) throws Exception {
        Optional<Role> roleWithName = this.roleRepository.findById(role.getName());
        if(roleWithName.isPresent())
            throw new Exception("roleNameAlreadyExist");
        return this.roleRepository.save(role);
    }

    public void remove(String roleName) throws Exception {
        Role role = this.roleRepository.findById(roleName).orElseThrow(()-> new Exception("roleNameNotFound"));
        List<User> users = this.userRepository.findByRoleName(roleName);
        if(!users.isEmpty())
            throw new Exception("usersAssociatedWithRole");
        this.roleRepository.delete(role);
    }
}
