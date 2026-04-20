package com.demo.tokenization.app.service;

import com.demo.tokenization.app.exception.TokenizationException;
import com.demo.tokenization.app.model.entity.TokenEntity;
import com.demo.tokenization.app.repositories.TokenRepository;
import com.demo.tokenization.app.util.EncryptionUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
public class TokenizationService
{
    private final TokenRepository tokenRepository;
    private final EncryptionUtils encryptionUtils; // Component to handle AES/RSA logic

    public TokenizationService(TokenRepository tokenRepository, EncryptionUtils encryptionUtils)
    {
        this.tokenRepository = tokenRepository;
        this.encryptionUtils = encryptionUtils;
    }

    /**
     * Swaps a collection of account numbers for tokens.
     */
    public List<String> tokenize(List<String> accountNumbers)
    {
            List<TokenEntity> tokenEntities = accountNumbers.stream()
                    .map(pan -> {
                        String token = UUID.randomUUID().toString();
                        String encrypted = encryptionUtils.encrypt(pan);
                        return new TokenEntity(token, encrypted);
                    })
                    .collect(Collectors.toList());
            return tokenRepository.saveAll(tokenEntities)
                    .stream()
                    .map(TokenEntity::getToken)
                    .collect(Collectors.toList());
    }

    /**
     * Swap back the tokens for the original account numbers.
     */
    public List<String> detokenize(List<String> tokens) throws TokenizationException
    {
        return tokens.stream()
                .map(token -> {
                    try {
                        return tokenRepository.findByToken(token)
                                .map(tokenEntity -> encryptionUtils.decrypt(tokenEntity.getEncryptedPan()))
                                .orElseThrow(() -> new TokenizationException("Invalid token: ", token));
                    } catch (TokenizationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
