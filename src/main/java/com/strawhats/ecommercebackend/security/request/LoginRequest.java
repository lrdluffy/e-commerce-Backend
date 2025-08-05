package com.strawhats.ecommercebackend.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Login Request entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(example = "lrdluffy")
    private String username;
    @Schema(example = "lrdPass")
    private String password;
}
