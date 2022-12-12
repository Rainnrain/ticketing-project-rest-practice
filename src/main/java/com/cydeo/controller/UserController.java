package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Tag(name="UserController", description="User API")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @ExecutionTime
    @GetMapping
    @RolesAllowed({"Manager", "Admin"})
    @Operation(summary="Get user")
    public ResponseEntity <ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList= userService.listAllUsers();
       return ResponseEntity.ok(new ResponseWrapper("User list", userDTOList, HttpStatus.OK));
    }

    @ExecutionTime
    @GetMapping("/{userName}")
    @RolesAllowed( "Admin")
    @Operation(summary="Get user by name")
    public ResponseEntity <ResponseWrapper> getUserByUserName(@PathVariable String userName){
        UserDTO userDTO= userService.findByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User by name", HttpStatus.OK));
    }
    @PostMapping
    @RolesAllowed( "Admin")
    @Operation(summary="Create user")
    public ResponseEntity <ResponseWrapper> createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User created", HttpStatus.CREATED));
    }
    @PutMapping
    @RolesAllowed( "Admin")
    @Operation(summary="Update User")
    public ResponseEntity <ResponseWrapper> updateUser(@RequestBody UserDTO userDTO){
        UserDTO userDTOUpdated= userService.update(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User Updated", userDTOUpdated, HttpStatus.OK));
    }
    @DeleteMapping("{userName}")
    @RolesAllowed( "Admin")
    @Operation(summary="Delete user")
    public ResponseEntity <ResponseWrapper> deleteUser(@PathVariable String userName) throws TicketingProjectException {
        userService.delete(userName);
        return ResponseEntity.ok(new ResponseWrapper("User list",  HttpStatus.OK));
    }
}
