package com.wenyao.swrasp.lexer;

public class LexerException extends Exception {
    public Token token;

    LexerException(String message, Token token) {
        super(String.format("Error at line %d, column %d: %s. Token: %s", token.line, token.column, message, token));
        this.token = token;
    }
}