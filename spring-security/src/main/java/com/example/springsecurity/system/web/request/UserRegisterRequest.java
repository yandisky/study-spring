package com.example.springsecurity.system.web.request;

import com.example.springsecurity.system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public User toUser() {
        return User.builder().username(this.getUsername()).enabled(true).build();
    }
}
