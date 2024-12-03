package com.qlsv.restcontroller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qlsv.model.JWTAuthResponse;
import com.qlsv.model.LoginModel;
import com.qlsv.service.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private AuthService authService;

    // Build Login REST API
    @PostMapping("login")
    public ResponseEntity<JWTAuthResponse> authenticate(@RequestBody LoginModel loginDto) {
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}
