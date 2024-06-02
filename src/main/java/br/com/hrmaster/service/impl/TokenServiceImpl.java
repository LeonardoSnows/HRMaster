package br.com.hrmaster.service.impl;

import br.com.hrmaster.model.PasswordResetToken;
import br.com.hrmaster.repository.TokenRepository;
import br.com.hrmaster.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenRepository repository;

    @Override
    public PasswordResetToken getToken(String token) {
        return repository.findByToken(token);
    }
}
