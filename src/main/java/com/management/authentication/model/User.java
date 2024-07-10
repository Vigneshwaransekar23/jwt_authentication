package com.management.authentication.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    @NotBlank
    private String username;
    @Size(max = 120)
    @NotBlank
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(max = 50) @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@Size(max = 50) @NotBlank String username) {
        this.username = username;
    }

    public @Size(max = 120) @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@Size(max = 120) @NotBlank String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
