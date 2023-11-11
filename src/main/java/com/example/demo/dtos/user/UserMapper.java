package com.example.demo.dtos.user;

import com.example.demo.models.Role;
import com.example.demo.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User createUserDTOToUser(CreateUserDTO createUserDTO){
        User user = new User();
        Role role = new Role();
        user.setName(createUserDTO.getName());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());
        user.setAddress(createUserDTO.getAddress());
        role.setName(createUserDTO.getRoleName());
        user.setRole(role);
        return user;
    };
    public static GetUserDTO userToGetUserDTO(User user){
        GetUserDTO getUserDTO = new GetUserDTO();
        getUserDTO.setEmail(user.getEmail());
        getUserDTO.setName(user.getName());
        getUserDTO.setAddress(user.getAddress());
        getUserDTO.setLastConnection(user.getLastConnection());
        getUserDTO.setCreatedAt(user.getCreatedAt());
        getUserDTO.setUpdatedAt(user.getUpdatedAt());
        getUserDTO.setRoleName(user.getRole().getName());
        return getUserDTO;
    };

    public static List<GetUserDTO> userListToGetUserDTOList(List<User> users){
        List<GetUserDTO> getUserDTOs = new ArrayList<>();
        for (User userItem: users) {
            GetUserDTO getUserDTO = UserMapper.userToGetUserDTO(userItem);
            getUserDTOs.add(getUserDTO);
        }
        return getUserDTOs;
    };

    public static User updateUserDTOToUser(UpdateUserDTO updateUserDTO){
        User user = new User();
        Role role = new Role();
        user.setName(updateUserDTO.getName());
        user.setAddress(updateUserDTO.getAddress());
        role.setName(updateUserDTO.getRoleName());
        user.setRole(role);
        return user;
    };
}
