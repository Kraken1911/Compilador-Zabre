package Analizers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alexico {

    public ArrayList<Token> getTokens(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        int start = 0;

        while (start < input.length()) {
            boolean matched = false;
            for (TokenType tokenType : TokenType.values()) {
                Pattern pattern = tokenType.getPattern();
                Matcher matcher = pattern.matcher(input.substring(start));
                if (matcher.lookingAt()) {
                    matched = true;
                    String tokenValue = matcher.group().trim();
                    Token token = new Token(tokenType, tokenValue);
                    tokens.add(token);
                    start += tokenValue.length();
                    break;
                }
            }
            if (!matched) {
                int end = start;
                while (end < input.length() && Character.isLetterOrDigit(input.charAt(end))) {
                    end++;
                }
                if (end > start) {
                    String tokenValue = input.substring(start, end).trim();
                    // Cambiamos de IDENTIFIER a VARIABLE para los tokens que coincidan con el patrón de variables.
                    Token token = new Token(TokenType.IDENTIFIER, tokenValue);
                    tokens.add(token);
                    start = end;
                } else {
                    System.err.println("Caracter de espacio en el indice: " + start);
                    start++;
                }
            }
        }
        return tokens;
    }

    public enum TokenType {
    	BOOLEAN_LITERAL("\\b(true|false)\\b"),
        WRONG_ASSIGNMENT_BOOLEAN("\\b(true|false)[a-zA-Z0-9_]+|[a-zA-Z0-9_]+(true|false)\\b"),
        VARIABLE("[a-z][a-z0-9_]*"),
        IDENTIFIER("(?!(true|false)\\b)[A-Z][A-Z0-9_]*"),
        ENTRADA("ENTRADA"),
        RETORNA("RETORNA"),
        IMPRIME("IMPRIME"),
        SI("SI"),
        HAZ("HAZ"),
        SINO("SINO"),
        BUCLE("BUCLE"),
        ENT("ENT"),
        DEC("DEC"),
        CADENA("CADENA"),
        BOOL("BOOL"),
        ASSIGN_OP("=="),
        ARITHMETIC_OP("[+\\-*/=<>!]+"),
        NUMBER("\\b\\d+\\.?\\d*\\b"),
        STRING("\"[^\"]*\""),
        LPAREN("\\("),
        RPAREN("\\)"),
        LBRACE("\\{"),
        RBRACE("\\}"),
        SEMICOLON(";"),
        COMMA(","),
        DEVUELVE("DEVUELVE");  

        private final Pattern pattern;

        TokenType(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public static class Token {
        public final TokenType type;
        public final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
