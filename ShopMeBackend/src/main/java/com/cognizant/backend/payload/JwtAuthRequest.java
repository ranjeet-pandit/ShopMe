package com.cognizant.backend.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JwtAuthRequest {

    private String email;

    private String password;
}
