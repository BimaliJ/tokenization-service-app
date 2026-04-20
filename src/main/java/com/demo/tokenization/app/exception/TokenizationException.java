package com.demo.tokenization.app.exception;

public class TokenizationException extends RuntimeException
{
    public TokenizationException(Exception e)
    {
        super(e);
    }

    public TokenizationException(String message, Object... params)
    {
        super(String.format(message, params));
    }
}
