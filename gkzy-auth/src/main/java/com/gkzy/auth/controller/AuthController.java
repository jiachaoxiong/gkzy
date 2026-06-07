package com.gkzy.auth.controller;

import com.gkzy.auth.dto.LoginDTO;
import com.gkzy.auth.dto.LoginVO;
import com.gkzy.auth.dto.RegisterDTO;
import com.gkzy.auth.entity.User;
import com.gkzy.auth.service.AuthService;
import com.gkzy.common.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public R<LoginVO> register(@Valid @RequestBody RegisterDTO dto) {
        return R.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(authService.login(dto));
    }

    @GetMapping("/me")
    public R<User> me(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return R.ok(authService.getCurrentUser(userId));
    }

    @PutMapping("/me")
    public R<Void> updateProfile(@RequestBody User user, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        authService.updateProfile(userId, user);
        return R.ok();
    }
}
