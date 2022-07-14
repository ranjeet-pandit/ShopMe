package com.cognizant.backend.payload;

import com.cognizant.backend.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private long id;

    @NotEmpty(message = "First Name must not be empty.")
    private String firstName;

    @NotEmpty(message = "Last Name must not be empty.")
    private String lastName;

    @NotEmpty(message = "Email address must not be empty.")
    @Email(message ="Email address is not valid.")
    private String email;

    @NotEmpty(message = "Password address must not be empty.")
    @Size(min = 5 , message = "Password must be min of 4 chars.")
    private String password;

    private Set<RoleDto> roles = new HashSet<>();

}
