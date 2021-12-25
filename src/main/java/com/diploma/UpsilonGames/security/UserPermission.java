package com.diploma.UpsilonGames.security;

public enum UserPermission {
    USER("USER"),
    ADMIN("ADMIN");

    private final String permission;

    UserPermission(String permission){
        this.permission = permission;
    }
}
