package com.example.springsecurity.system.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("USER"),
    ADMIN("ADMIN");
    String name;

    RoleType(String name) {
        this.name = name;
    }
}
