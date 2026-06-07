package com.gkzy.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank @Size(min = 2, max = 50)
    private String username;
    @NotBlank @Size(min = 6, max = 32)
    private String password;
    private String nickname;
    @NotBlank
    private String phone;
    private String subjectType;
}
