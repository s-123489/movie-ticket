package com.zjsu.syt.cinema.user.dto;

/**
 * 登录响应DTO
 */
public record LoginResponse(
    String token,
    String userId,
    String username,
    String name,
    String email
) {}
