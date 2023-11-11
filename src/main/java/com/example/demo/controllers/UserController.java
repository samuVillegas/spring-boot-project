package com.example.demo.controllers;

import com.example.demo.dtos.user.CreateUserDTO;
import com.example.demo.dtos.user.GetUserDTO;
import com.example.demo.dtos.user.UpdateUserDTO;
import com.example.demo.dtos.user.UserMapper;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "List all users", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Users obtained successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GetUserDTO.class))
                            )
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = {@Content()})
    })
    @GetMapping()
    public ResponseEntity<List<GetUserDTO>> findAll() {
        try {
            List<User> users = this.userService.findAll();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserMapper.userListToGetUserDTOList(users));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
        }
    }

    @Operation(summary = "Create user", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User created successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetUserDTO.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "432", description = "Role name not found", content = {@Content()}),
            @ApiResponse(responseCode = "484", description = "Email already exist", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = {@Content()})
    })
    @PostMapping()
    public ResponseEntity<GetUserDTO> create(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            User newUser = this.userService.save(UserMapper.createUserDTOToUser(createUserDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.userToGetUserDTO(newUser));
        } catch (Exception e) {
            switch (e.getMessage()) {
                case "roleNameNotFound" -> throw new ResponseStatusException(HttpStatusCode.valueOf(432));
                case "emailAlreadyExist" -> throw new ResponseStatusException(HttpStatusCode.valueOf(484));
                default -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
            }
        }
    }

    @Operation(summary = "Update user", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User updated successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetUserDTO.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "432", description = "Email not found", content = {@Content()}),
            @ApiResponse(responseCode = "433", description = "Role name not found", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = {@Content()})
    })
    @PatchMapping("/{email}")
    public ResponseEntity<GetUserDTO> update(@PathVariable("email") String email, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            User user = UserMapper.updateUserDTOToUser(updateUserDTO);
            user.setEmail(email);
            User userUpdated = this.userService.update(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserMapper.userToGetUserDTO(userUpdated));
        } catch (Exception e) {
            switch (e.getMessage()) {
                case "emailNotFound" -> throw new ResponseStatusException(HttpStatusCode.valueOf(432));
                case "roleNameNotFound" -> throw new ResponseStatusException(HttpStatusCode.valueOf(433));
                default -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
            }
        }
    }
}
