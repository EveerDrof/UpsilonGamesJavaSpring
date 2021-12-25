package com.diploma.UpsilonGames.security;

import com.google.common.collect.Sets;

import java.util.Set;

public enum UserRole {
    USER(Sets.newHashSet(UserPermission.USER)),
    ADMIN(Sets.newHashSet(UserPermission.ADMIN));
    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }
}
