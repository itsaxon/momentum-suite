package com.momentum.suite.domain.user.model;

import lombok.Getter;

@Getter
public enum UserStatus {
    ENABLED("启用"),
    DISABLED("禁用"),
    LOCKED("锁定");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}
