package com.gkzy.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 修改个人信息DTO — 仅允许修改安全字段，禁止修改用户名、密码等
 */
@Data
public class UpdateProfileDTO {
    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String subjectType;
}
