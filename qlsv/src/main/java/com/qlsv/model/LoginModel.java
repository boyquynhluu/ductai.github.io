package com.qlsv.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginModel {
    private String usernameOrEmail;
    private String password;
}
