package com.gkzy.auth.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gkzy.auth.dto.LoginDTO;
import com.gkzy.auth.dto.LoginVO;
import com.gkzy.auth.dto.RegisterDTO;
import com.gkzy.auth.dto.UpdateProfileDTO;
import com.gkzy.auth.entity.User;
import com.gkzy.auth.mapper.UserMapper;
import com.gkzy.auth.util.JwtUtil;
import com.gkzy.common.BusinessException;
import com.gkzy.common.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginVO register(RegisterDTO dto) {
        if (userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()))) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        if (userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()))) {
            throw new BusinessException(ResultCode.PHONE_EXISTS);
        }
        User user = BeanUtil.copyProperties(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setProvince("山西");
        userMapper.insert(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    public LoginVO login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    public User getCurrentUser(Long userId) {
        return userMapper.selectById(userId);
    }

    public void updateProfile(Long userId, UpdateProfileDTO dto) {
        User user = userMapper.selectById(userId);
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getSubjectType() != null) user.setSubjectType(dto.getSubjectType());
        userMapper.updateById(user);
    }
}
