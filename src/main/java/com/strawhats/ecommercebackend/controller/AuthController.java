package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.model.AppRole;
import com.strawhats.ecommercebackend.model.Role;
import com.strawhats.ecommercebackend.model.User;
import com.strawhats.ecommercebackend.payload.UserDTO;
import com.strawhats.ecommercebackend.repository.RoleRepository;
import com.strawhats.ecommercebackend.repository.UserRepository;
import com.strawhats.ecommercebackend.security.jwt.JwtUtils;
import com.strawhats.ecommercebackend.security.request.LoginRequest;
import com.strawhats.ecommercebackend.security.request.RegisterRequest;
import com.strawhats.ecommercebackend.security.response.JwtResponse;
import com.strawhats.ecommercebackend.security.response.MessageResponse;
import com.strawhats.ecommercebackend.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "JWT Authorization")
@Tag(name = "\uD83D\uDC64 User Management Module", description = "APIs & Rest Endpoints related to User Management")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    @Operation(summary = "Login User", description = "Login and get JWT")
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login Object which includes Credentials", required = true)
            @RequestBody LoginRequest loginRequest
    ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            log.error("Invalid username or password");
            Map<String, Object> response = new HashMap<>();
            response.put("Status", HttpServletResponse.SC_UNAUTHORIZED);
            response.put("Message", e.getMessage());
            response.put("Error", "Invalid username or password");
            response.put("path", "/api/auth/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setUserId(userDetails.getId());
        jwtResponse.setUserName(userDetails.getUsername());
        jwtResponse.setJwtToken(jwtToken);
        jwtResponse.setRoles(roles);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Credentials already taken")
    })
    @Operation(summary = "Register User", description = "Register a new user")
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Register Object which includes User's info", required = true)
            @RequestBody RegisterRequest registerRequest
    ) {
        if (userRepository.existsUserByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("User already taken"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsUserByEmail(registerRequest.getEmail())){
            return new ResponseEntity<>(new MessageResponse("Email already taken"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        List<String> strRoles = registerRequest.getRoles();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findRoleByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findRoleByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findRoleByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findRoleByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User registeredUser = userRepository.save(user);

        UserDTO registerUserDTO = modelMapper.map(registeredUser, UserDTO.class);
        return new ResponseEntity<>(registerUserDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Authorization Error occured")
    })
    @Operation(summary = "Get User Profile", description = "Get user profile by ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserProfile(
            @Parameter(name = "userId", description = "Id of user to be retrieved")
            @PathVariable Long userId,
            Authentication authentication
    ){
        if (authentication == null) {
            return new ResponseEntity<>(new MessageResponse("Authentication object is Null"), HttpStatus.UNAUTHORIZED);
        }

        System.out.println(authentication.toString());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.getId().equals(userId)) {
            return new ResponseEntity<>(new MessageResponse("Wrong userId"), HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
