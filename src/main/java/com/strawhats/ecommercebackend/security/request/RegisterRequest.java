package com.strawhats.ecommercebackend.security.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Schema(description = "Register Request entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Schema(example = "lrdluffy")
    private String username;
    @Schema(example = "lrdluffy@gmail.com")
    private String email;
    @Schema(example = "lrdPass")
    private String password;
    @Schema(example = "[\"user\"]")
    private List<String> roles;
}
