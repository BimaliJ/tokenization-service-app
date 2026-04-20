package com.demo.tokenization.app.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TokenizedResponse
{
    private List<String> tokens;
}
