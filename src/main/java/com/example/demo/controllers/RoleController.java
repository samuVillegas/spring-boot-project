package com.example.demo.controllers;

import com.example.demo.dtos.role.CreateRoleDTO;
import com.example.demo.dtos.role.RoleMapper;
import com.example.demo.dtos.user.GetUserDTO;
import com.example.demo.models.Role;
import com.example.demo.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "List all roles", tags = "Roles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Roles obtained successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Role.class))
                            )
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = {@Content()})
    })
    @GetMapping
    public ResponseEntity<List<Role>> findAll(){
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.roleService.findAll());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };

    @Operation(summary = "Create role", tags = "Roles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Role created successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Role.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "484", description = "Role name already exist", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = {@Content()})
    })
    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody CreateRoleDTO createRoleDTO){
        try{
            Role newRole = RoleMapper.createRoleDTOToRole(createRoleDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.roleService.save(newRole));
        }catch (Exception e) {
            if (e.getMessage().equals("roleNameAlreadyExist")) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(484));
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
        }
    };


    @Operation(summary = "Delete role", tags = "Roles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Role deleted successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "432", description = "Role name not found", content = {@Content()}),
            @ApiResponse(responseCode = "489", description = "Users associated with role", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = {@Content()})
    })
    @DeleteMapping("/{role_name}")
    public ResponseEntity<String> delete(@PathVariable("role_name") String roleName){
        try {
            this.roleService.remove(roleName);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Role deleted successfully");
        } catch (Exception e) {
            switch (e.getMessage()) {
                case "roleNameNotFound" -> throw new ResponseStatusException(HttpStatusCode.valueOf(432));
                case "usersAssociatedWithRole" -> throw new ResponseStatusException(HttpStatusCode.valueOf(489));
                default -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
            }
        }
    };
}
