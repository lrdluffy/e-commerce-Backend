package com.strawhats.ecommercebackend.payload;

import com.strawhats.ecommercebackend.model.Address;
import com.strawhats.ecommercebackend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private List<Role> roles;
    private List<Address> addresses;
}
