package com.demo.tokenization.app.service;

import com.demo.tokenization.app.model.entity.TokenEntity;
import com.demo.tokenization.app.repositories.TokenRepository;
import com.demo.tokenization.app.util.EncryptionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenizationServiceTest {
    @InjectMocks
    private TokenizationService tokenizationService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private EncryptionUtils encryptionUtils;

    @Test
    void test_tokenize_success()
    {
        //given
        List<String> accountNumbers = List.of("acc1", "acc2", "acc3");
        TokenEntity tokenEntity1 = TokenEntity.builder().token("xxx").encryptedPan("ddd").build();
        TokenEntity tokenEntity2 = TokenEntity.builder().token("yyy").encryptedPan("sss").build();
        TokenEntity tokenEntity3 = TokenEntity.builder().token("zzz").encryptedPan("jjj").build();

        List<TokenEntity> tokenEntities = List.of(tokenEntity1, tokenEntity2, tokenEntity3);

        when(encryptionUtils.encrypt(any(String.class)))
                .thenReturn("ddd", "sss", "jjj");
        when(tokenRepository.saveAll(any())).thenReturn(tokenEntities);

        //when
        tokenizationService.tokenize(accountNumbers);

        //then
        verify(encryptionUtils, times(accountNumbers.size()))
                .encrypt(anyString());
        verify(tokenRepository).saveAll(anyList());
    }

    @Test
    void test_deTokenize_success()
    {
        //given
        List<String> tokens = List.of("xxx", "yyy", "zzz");
        TokenEntity tokenEntity1 = TokenEntity.builder().token("xxx").encryptedPan("ddd").build();
        TokenEntity tokenEntity2 = TokenEntity.builder().token("yyy").encryptedPan("sss").build();
        TokenEntity tokenEntity3 = TokenEntity.builder().token("zzz").encryptedPan("jjj").build();

        when(encryptionUtils.decrypt(any(String.class)))
                .thenReturn("acc1", "acc2", "acc3");
        when(tokenRepository.findByToken(any(String.class)))
                .thenReturn(Optional.of(tokenEntity1))
                .thenReturn(Optional.of(tokenEntity2))
                .thenReturn(Optional.of(tokenEntity3));

        //when
        tokenizationService.detokenize(tokens);

        //then
        verify(encryptionUtils, times(tokens.size()))
                .decrypt(anyString());
        verify(tokenRepository, times(tokens.size()))
                .findByToken(anyString());
    }
}
