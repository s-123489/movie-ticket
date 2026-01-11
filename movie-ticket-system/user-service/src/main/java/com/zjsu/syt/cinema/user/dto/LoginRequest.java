package com.zjsu.syt.cinema.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 */
public record LoginRequest(
    @NotBlank(message = "用户名不能为空")
    String username,

    @NotBlank(message = "密码不能为空")
    String password
) {}
