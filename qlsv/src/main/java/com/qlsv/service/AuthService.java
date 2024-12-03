package com.qlsv.service;

import com.qlsv.model.LoginModel;

public interface AuthService {
    String login(LoginModel loginDto);
}
