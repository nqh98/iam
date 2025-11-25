package com.huynq.iam.core.domain.service;

public interface PasswordService {
    String encode(String plainPassword);

    boolean matches(String plainPassword, String encodedPassword);
}
