package com.example.springsecurity.system.exception;

import java.util.Map;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(Map<String, Object> data) {
        super(ErrorCode.ROLE_NOT_FOUND, data);
    }
}
