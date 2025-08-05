package com.strawhats.ecommercebackend.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private Long userId;
    private String userName;
    private String jwtToken;
    private List<String> roles;
}
