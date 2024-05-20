package com.wenyao.swrasp.lexer;

import java.util.List;

public class LexerHelper {
    public static String dumpTokens(List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.value);
        }
        return sb.toString();
    }

    public static List<Token> lexer(String input) throws LexerException {
        Lexer lexer = new Lexer(input);
        return lexer.tokenize();
    }
}