package com.wenyao.swrasp.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position;
    private int length;
    private int line;
    private int column;

    Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.length = input.length();
        this.line = 1;
        this.column = 1;
    }

    public List<Token> tokenize() throws LexerException {
        List<Token> tokens = new ArrayList<>();
        while (position < length) {
            char currentChar = input.charAt(position);
            if (Character.isWhitespace(currentChar)) {
                tokens.add(new Token(Token.Type.WHITESPACE, consumeWhile(Character::isWhitespace), line, column));
            } else if (currentChar == '(' || currentChar == ')') {
                tokens.add(new Token(Token.Type.PARENTHESIS, String.valueOf(currentChar), line, column));
                advancePosition();
            } else if (currentChar == '"' || currentChar == '\'') {
                tokens.add(new Token(Token.Type.STRING, consumeString(currentChar), line, column));
            } else if (Character.isDigit(currentChar)) {
                tokens.add(new Token(Token.Type.NUMBER, consumeWhile(Character::isDigit), line, column));
            } else if (isOperatorStart(currentChar)) {
                tokens.add(new Token(Token.Type.OPERATOR, consumeWhile(this::isOperatorPart), line, column));
            } else if (Character.isLetter(currentChar)) {
                tokens.add(new Token(Token.Type.IDENTIFIER, consumeWhile(Character::isLetterOrDigit), line, column));
            } else if (isIdentifier(currentChar)) {
                tokens.add(new Token(Token.Type.IDENTIFIER, consumeWhile(this::isIdentifier), line, column));
            } else {
                throw new LexerException("Unexpected character", new Token(null, String.valueOf(currentChar), line, column));
            }
        }
        checkBalancedParentheses(tokens);
        return tokens;
    }

    private boolean isIdentifier(char c) {
        return c == '_' || c == ',' || c == '*' || c == '.' || c == '-' || c == '+' || c == '/' || c == '%' || c == '!' || c == '<' || c == '>' || c == '?' || c == '@' || c == '[' || c == ']' || c == '^' || c == '`' || c == '{' || c == '}' || c == '~' || c == ';';
    }

    private String consumeWhile(java.util.function.Predicate<Character> condition) {
        StringBuilder sb = new StringBuilder();
        while (position < length && condition.test(input.charAt(position))) {
            sb.append(input.charAt(position));
            advancePosition();
        }
        return sb.toString();
    }

    private String consumeString(char quoteType) throws LexerException {
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startColumn = column;
        sb.append(input.charAt(position)); // consume opening quote
        advancePosition();
        while (position < length && input.charAt(position) != quoteType) {
            sb.append(input.charAt(position));
            advancePosition();
        }
        if (position < length) {
            sb.append(input.charAt(position)); // consume closing quote
            advancePosition();
        } else {
            throw new LexerException("Unclosed string literal", new Token(Token.Type.STRING, sb.toString(), startLine, startColumn));
        }
        return sb.toString();
    }

    private void advancePosition() {
        if (input.charAt(position) == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        position++;
    }

    private boolean isOperatorStart(char ch) {
        return ch == '=' || ch == 'o' || ch == 'a';
    }

    private boolean isOperatorPart(char ch) {
        return ch == '=' || ch == 'o' || ch == 'r' || ch == 'a' || ch == 'n' || ch == 'd';
    }

    private void checkBalancedParentheses(List<Token> tokens) throws LexerException {
        int balance = 0;
        for (Token token : tokens) {
            if (token.type == Token.Type.PARENTHESIS) {
                if (token.value.equals("(")) {
                    balance++;
                } else if (token.value.equals(")")) {
                    balance--;
                }
                if (balance < 0) {
                    throw new LexerException("Unbalanced parentheses: too many closing parentheses", token);
                }
            }
        }
        if (balance != 0) {
            throw new LexerException("Unbalanced parentheses: too many opening parentheses", new Token(null, "(", line, column));
        }
    }
}