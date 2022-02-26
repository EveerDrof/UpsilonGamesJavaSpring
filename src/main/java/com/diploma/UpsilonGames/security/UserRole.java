package com.diploma.UpsilonGames.security;

import com.google.common.collect.Sets;

import java.util.Set;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");
    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
