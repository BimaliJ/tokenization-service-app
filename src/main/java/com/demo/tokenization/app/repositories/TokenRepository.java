package com.demo.tokenization.app.repositories;

import com.demo.tokenization.app.model.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity,Long>
{
    /**
     * Finds a token by the unique token string which can
     * be used to detokenize the token
     */
    Optional<TokenEntity> findByToken(String token);
}
