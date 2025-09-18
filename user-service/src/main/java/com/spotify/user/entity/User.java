package com.spotify.user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public UUID getId () {
        return id;
    }

    public void setId (UUID id) {
        this.id = id;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getRole () {
        return role;
    }

    public void setRole (String role) {
        this.role = role;
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    public String getPasswordHash () {
        return passwordHash;
    }

    public void setPasswordHash (String passwordHash) {
        this.passwordHash = passwordHash;
    }

    private String passwordHash;

    private String role = "FREE";
}

