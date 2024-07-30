package org.example.studyenglishjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度必须在3到20个字符之间")
        String username,

        @NotBlank(message = "密码不能为空")
        @Size(min = 3, max = 30, message = "密码长度必须在3到30个字符之间")
        String password) {

}