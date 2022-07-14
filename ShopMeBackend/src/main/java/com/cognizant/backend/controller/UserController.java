package com.cognizant.backend.controller;

import com.cognizant.backend.model.User;
import com.cognizant.backend.payload.ApiResponse;
import com.cognizant.backend.payload.UserDto;
import com.cognizant.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(methods = RequestMethod.GET)
public class UserController {

    @Autowired
    private UserService userService;

    //post - create user
    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto , HttpStatus.CREATED);
    }

    //put - update user
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto , @PathVariable Long userId){
        UserDto updateUser = this.userService.updateUser(userDto , userId);
        return new ResponseEntity<>(updateUser , HttpStatus.OK);
    }

    //delete - delete user
    //ADMIN
    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity<>( new ApiResponse("User Deleted Successfully" , true) , HttpStatus.OK);
    }


    //get - user get
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }
}
