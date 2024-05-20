package com.wenyao.swrasp.lexer;

public class Token {
    public enum Type {
        IDENTIFIER, STRING, NUMBER, OPERATOR, PARENTHESIS, WHITESPACE
    }

    public Type type;
    public String value;
    public int line;
    public int column;

    Token(Type type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("Token{%s, '%s', line: %d, column: %d}", type, value, line, column);
    }
}