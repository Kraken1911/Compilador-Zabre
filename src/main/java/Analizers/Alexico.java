package Analizers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alexico {

    public ArrayList<Token> getTokens(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        int start = 0;
//
        while (start < input.length()) {// mientras sea menor a la longitud del texto 
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
                // Detectar identificadores no reservados
                int end = start;
                while (end < input.length() && Character.isLetterOrDigit(input.charAt(end))) {
                    end++;
                }
                if (end > start) {
                    String tokenValue = input.substring(start, end).trim();
                    tokens.add(new Token(TokenType.IDENTIFIER, tokenValue));
                    start = end;
                } else {
                    System.err.println("Caracter inválido en la posición " + start + ": " + input.charAt(start));
                    start++;  // Avanzar al siguiente carácter
                }
            }
        }
        return tokens;
    }

    public enum TokenType {
        IDENTIFIER("\\b[a-zA-Z][a-zA-Z0-9]*\\b"),
        ENTRADA("\\b(ENTRADA)\\s*\\(\\s*([a-zA-Z][a-zA-Z0-9]*)\\s*\\)\\s*;"),
        RETORNA("\\b(RETORNA)\\s*\\(\\s*([a-zA-Z0-9\\s+\\-\\*/]*)\\s*\\)\\s*;"),
        IMPRIME("\\b(IMPRIME)\\s*\\(\\s*([a-zA-Z0-9\\s+\\-\\*/\"'()]*)\\s*\\)\\s*;"),
        SI("\\b(SI)\\s*\\(\\s*([a-zA-Z][a-zA-Z0-9\\s+\\-\\*/><=!&|]*)\\s*\\)\\s*\\b(HAZ)\\b\\s*\\{([a-zA-Z0-9\\s+\\-\\*/\"'();]*)\\}\\s*;"),
        SINO("\\b(SINO)\\s*\\{([a-zA-Z0-9\\s+\\-\\*/\"'();]*)\\}\\s*;"),
        CAD("\\b(CAD)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*\"[^\"]*\")\\s*;"),
        ENT("\\b(ENT)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*\\d+)\\s*;"),
        BOOL("\\b(BOOL)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*(true|false))\\s*;"),
        DEC("\\b(DEC)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*\\d+\\.\\d+)\\s*;"),
        ARITHMETIC_OP("\\b([a-zA-Z][a-zA-Z0-9]*\\s*[+\\-*/]\\s*[a-zA-Z][a-zA-Z0-9]*)\\s*;");

        private final Pattern pattern;

        TokenType(String regex) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
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
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
