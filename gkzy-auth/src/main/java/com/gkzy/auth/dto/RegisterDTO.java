package com.gkzy.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank @Size(min = 2, max = 50, message = "用户名长度2-50个字符")
    private String username;

    @NotBlank @Size(min = 8, max = 32, message = "密码长度8-32个字符")
    private String password;

    private String nickname;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号")
    private String phone;

    private String subjectType;
}
