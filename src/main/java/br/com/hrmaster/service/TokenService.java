package br.com.hrmaster.service;

import br.com.hrmaster.model.PasswordResetToken;

public interface TokenService {
    PasswordResetToken getToken(String token);
}
