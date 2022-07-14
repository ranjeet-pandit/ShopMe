package com.cognizant.backend.controller;

import com.cognizant.backend.BackendApplication;
import com.cognizant.backend.exception.ApiException;
import com.cognizant.backend.model.User;
import com.cognizant.backend.payload.JwtAuthRequest;
import com.cognizant.backend.payload.JwtAuthResponse;
import com.cognizant.backend.payload.UserDto;
import com.cognizant.backend.security.JwtTokenHelper;
import com.cognizant.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(methods = RequestMethod.POST)
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            ) throws Exception {
        this.authenticate(request.getEmail() , request.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());

        String token = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setUser(userDetails);

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    //post - register new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDto userDto){
        User createUser = this.userService.registerUser(userDto);
        return new ResponseEntity<>(createUser , HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username , password);

        try{
            this.authenticationManager.authenticate(authenticationToken);

        }catch (BadCredentialsException e){
            throw new ApiException("Invalid email or password");
        }


    }

}
