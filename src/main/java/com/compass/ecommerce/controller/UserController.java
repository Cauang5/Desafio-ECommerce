package com.compass.ecommerce.controller;

import com.compass.ecommerce.DTO.UserDTORequest;
import com.compass.ecommerce.DTO.UserDTOResponse;
import com.compass.ecommerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTOResponse> createUser(@Valid @RequestBody UserDTORequest userDTORequest) {
        UserDTOResponse userDTOResponse = userService.create(userDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTOResponse> getUserById(@PathVariable Long id) {
        UserDTOResponse userDTOResponse = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTOResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTORequest userDTORequest) {
        UserDTOResponse userDTOResponse = userService.updateUser(id, userDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTOResponse> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
