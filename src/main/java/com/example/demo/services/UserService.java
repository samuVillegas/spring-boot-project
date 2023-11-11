package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User save(User user) throws Exception {
        Role role = this.roleRepository.findById(user.getRole().getName()).orElseThrow(()->new Exception("roleNameNotFound"));
        Optional<User> userWithEmail = this.userRepository.findById(user.getEmail());
        if (userWithEmail.isPresent())
            throw new Exception("emailAlreadyExist");
        user.setRole(role);
        return this.userRepository.save(user);
    }

    public User update(User user) throws Exception {
        User userToUpdate = this.userRepository
                .findById(user.getEmail())
                .orElseThrow(() -> new Exception("emailNotFound"));
        if(user.getName() != null)
            userToUpdate.setName(user.getName());
        if(user.getAddress() != null)
            userToUpdate.setAddress(user.getAddress());
        if(user.getRole().getName() != null){
            Role role = this.roleRepository.findById(user.getRole().getName()).orElseThrow(() -> new Exception("roleNameNotFound"));
            userToUpdate.setRole(role);
        }
        return this.userRepository.save(userToUpdate);
    }
}
