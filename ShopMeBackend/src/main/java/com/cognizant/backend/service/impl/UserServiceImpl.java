package com.cognizant.backend.service.impl;

import com.cognizant.backend.config.AppConstants;
import com.cognizant.backend.exception.ResourceNotFoundException;
import com.cognizant.backend.model.Role;
import com.cognizant.backend.model.User;
import com.cognizant.backend.payload.UserDto;
import com.cognizant.backend.repository.RoleRepository;
import com.cognizant.backend.repository.UserRepository;
import com.cognizant.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        //encode the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        //roles
        Role role = this.roleRepository.findById(AppConstants.USER_ROLE_ID).get();
        user.getRoles().add(role);

        User savedUser = this.userRepository.save(user);

        return (savedUser);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = this.dtoToUser(userDto);
        //encode the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        User savedUser = this.userRepository.save(user);

        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User" , "id" , userId));

        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        User updatedUser = this.userRepository.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public User getUserById(long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User" , "id" , userId));
        return (user);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = this.userRepository.findAll();

        return users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long userId) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User" , "id" , userId));
        user.setRoles(null);
        this.userRepository.delete(user);
    }

    private User dtoToUser(UserDto userDto){
        return this.modelMapper.map(userDto, User.class);
    }

    private UserDto userToDto(User user){
        return this.modelMapper.map(user, UserDto.class);
    }

}
