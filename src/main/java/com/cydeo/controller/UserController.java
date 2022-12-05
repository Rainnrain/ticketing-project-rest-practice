package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity <ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList= userService.listAllUsers();
       return ResponseEntity.ok(new ResponseWrapper("User list", userDTOList, HttpStatus.OK));
    }

    @GetMapping("/{userName}")
    public ResponseEntity <ResponseWrapper> getUserByUserName(@PathVariable String userName){
        UserDTO userDTO= userService.findByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User by name", HttpStatus.OK));
    }
    @PostMapping
    public ResponseEntity <ResponseWrapper> createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User created", HttpStatus.CREATED));
    }
    @PutMapping
    public ResponseEntity <ResponseWrapper> updateUser(@RequestBody UserDTO userDTO){
        UserDTO userDTOUpdated= userService.update(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User Updated", userDTOUpdated, HttpStatus.OK));
    }
    @DeleteMapping("{userName}")
    public ResponseEntity <ResponseWrapper> deleteUser(@PathVariable String userName){
        userService.delete(userName);
        return ResponseEntity.ok(new ResponseWrapper("User list",  HttpStatus.OK));
    }
}
