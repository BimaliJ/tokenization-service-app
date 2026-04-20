package com.demo.tokenization.app.controller;

import com.demo.tokenization.app.exception.TokenizationException;
import com.demo.tokenization.app.model.DetokenizedResponse;
import com.demo.tokenization.app.model.DetokenizeRequest;
import com.demo.tokenization.app.model.TokenizeRequest;
import com.demo.tokenization.app.model.TokenizedResponse;
import com.demo.tokenization.app.service.TokenizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping
@RequiredArgsConstructor
public class TokenizationController
{
    private final TokenizationService tokenizationService;

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizedResponse> tokenizeData(@RequestBody TokenizeRequest tokenizeRequest)
    {
        List<String> accountNumbers = tokenizeRequest.getAccountNumbers();
        try {
            //Order updated = orderService.updateOrder(id, status);
            List<String> tokens = tokenizationService.tokenize(accountNumbers);
            return ResponseEntity.ok(TokenizedResponse.builder().tokens(tokens).build());
        }
        catch (Exception e) {
            log.error("Error while converting account {} to tokens", accountNumbers, e);
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    @PostMapping("/detokenize")
    public ResponseEntity<DetokenizedResponse> deTokenizeData(@RequestBody DetokenizeRequest detokenizeRequest)
    {
        List<String> tokens = detokenizeRequest.getTokens();
        try {
            List<String> accountNumbers = tokenizationService.detokenize(tokens);
            return ResponseEntity.ok(DetokenizedResponse.builder().accountNumbers(accountNumbers).build());
        }
        catch (TokenizationException e) {
            log.error("Error while converting tokens: {} to account numbers", tokens, e);
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
