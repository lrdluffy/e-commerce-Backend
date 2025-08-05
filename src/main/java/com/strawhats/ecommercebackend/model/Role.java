package com.strawhats.ecommercebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private AppRole roleName;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
