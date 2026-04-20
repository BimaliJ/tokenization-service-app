package com.demo.tokenization.app.controller;

import com.demo.tokenization.app.model.DetokenizeRequest;
import com.demo.tokenization.app.model.TokenizeRequest;
import com.demo.tokenization.app.service.TokenizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenizationControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenizationService tokenizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_tokenize_success() throws Exception
    {
        TokenizeRequest tokenizeRequest = TokenizeRequest.builder()
                .accountNumbers(List.of("4111-1111-1111-1111", "4444-3333-2222-1111", "4444-1111-2222-3333"))
                .build();
        List<String> tokens = List.of("{fvMymE7X0Je1IzMDgWooV5iGBPw0yoFy}", "L4hKuBJHxe67ENSKLVbdIH8NhFefPui2", "ZA5isc0kVUfvlxTE5m2dxIY8AG76KoP3");

        when(tokenizationService.tokenize(tokenizeRequest.getAccountNumbers()))
                .thenReturn(tokens);

        mockMvc.perform(post("/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenizeRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void test_deTokenize_success() throws Exception
    {
        DetokenizeRequest detokenizeRequest = DetokenizeRequest.builder()
                .tokens(List.of("{fvMymE7X0Je1IzMDgWooV5iGBPw0yoFy}", "L4hKuBJHxe67ENSKLVbdIH8NhFefPui2", "ZA5isc0kVUfvlxTE5m2dxIY8AG76KoP3"))
                .build();
        List<String> accountNumbers = List.of("4111-1111-1111-1111", "4444-3333-2222-1111", "4444-1111-2222-3333");

        when(tokenizationService.detokenize(detokenizeRequest.getTokens()))
                .thenReturn(accountNumbers);

        mockMvc.perform(post("/detokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detokenizeRequest)))
                .andExpect(status().isOk());
    }
}
