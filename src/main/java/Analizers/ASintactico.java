/*package Analizers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Analizers.Alexico.Token;

public class ASintactico {

    private ArrayList<Token> tokens;
    private int currentIndex;

    public boolean parse(String input) {
        Alexico alexico = new Alexico();
        tokens = alexico.getTokens(input);
        currentIndex = 0;

        while (currentIndex < tokens.size()) {
            if (!parseStatement()) {
                return false;
            }
        }
        return true;
    }

    private boolean parseStatement() {
        if (match(TokenType.ENTRADA)) {
            return parseEntrada();
        } else if (match(TokenType.RETORNA)) {
            return parseRetorna();
        } else if (match(TokenType.IMPRIME)) {
            return parseImprime();
        } else if (match(TokenType.SI)) {
            return parseSi();
        } else if (match(TokenType.SINO)) {
            return parseSino();
        } else if (match(TokenType.CAD)) {
            return parseCad();
        } else if (match(TokenType.ENT)) {
            return parseEnt();
        } else if (match(TokenType.BOOL)) {
            return parseBool();
        } else if (match(TokenType.DEC)) {
            return parseDec();
        } else {
            System.err.println("Error: unexpected token " + currentToken());
            return false;
        }
    }

    private boolean parseEntrada() {
        if (!expect(TokenType.ENTRADA)) return false;
        if (!expect(TokenType.IDENTIFIER)) return false;
        return expectSemicolon();
    }

    private boolean parseRetorna() {
        if (!expect(TokenType.RETORNA)) return false;
        return expectSemicolon();
    }

    private boolean parseImprime() {
        if (!expect(TokenType.IMPRIME)) return false;
        return expectSemicolon();
    }

    private boolean parseSi() {
        if (!expect(TokenType.SI)) return false;
        if (!expect(TokenType.IDENTIFIER)) return false;
        if (!expect(TokenType.HAZ)) return false;
        if (!expectOpenBrace()) return false;
        while (!match(TokenType.CLOSE_BRACE)) {
            if (!parseStatement()) return false;
        }
        return expectCloseBrace();
    }

    private boolean parseSino() {
        if (!expect(TokenType.SINO)) return false;
        if (!expectOpenBrace()) return false;
        while (!match(TokenType.CLOSE_BRACE)) {
            if (!parseStatement()) return false;
        }
        return expectCloseBrace();
    }

    private boolean parseCad() {
        if (!expect(TokenType.CAD)) return false;
        return expectSemicolon();
    }

    private boolean parseEnt() {
        if (!expect(TokenType.ENT)) return false;
        return expectSemicolon();
    }

    private boolean parseBool() {
        if (!expect(TokenType.BOOL)) return false;
        return expectSemicolon();
    }

    private boolean parseDec() {
        if (!expect(TokenType.DEC)) return false;
        return expectSemicolon();
    }

    private boolean expect(TokenType type) {
        if (match(type)) {
            currentIndex++;
            return true;
        } else {
            System.err.println("Error: expected " + type + " but found " + currentToken());
            return false;
        }
    }

    private boolean expectSemicolon() {
        return expect(TokenType.SEMICOLON);
    }

    private boolean expectOpenBrace() {
        return expect(TokenType.OPEN_BRACE);
    }

    private boolean expectCloseBrace() {
        return expect(TokenType.CLOSE_BRACE);
    }

    private boolean match(TokenType type) {
        return currentIndex < tokens.size() && tokens.get(currentIndex).type == type;
    }

    private Token currentToken() {
        if (currentIndex < tokens.size()) {
            return tokens.get(currentIndex);
        }
        return null;
    }

    public enum TokenType {
        IDENTIFIER("\\b[a-zA-Z][a-zA-Z0-9]*\\b"),
        ENTRADA("\\bENTRADA\\b"),
        RETORNA("\\bRETORNA\\b"),
        IMPRIME("\\bIMPRIME\\b"),
        SI("\\bSI\\b"),
        SINO("\\bSINO\\b"),
        CAD("\\bCAD\\b"),
        ENT("\\bENT\\b"),
        BOOL("\\bBOOL\\b"),
        DEC("\\bDEC\\b"),
        SEMICOLON(";"),
        OPEN_BRACE("\\{"),
        CLOSE_BRACE("\\}");

        private final Pattern pattern;

        TokenType(String regex) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
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

    public static void main(String[] args) {
        ASintactico parser = new ASintactico();
        String code = "ENTRADA (x); RETORNA (5); IMPRIME (\"Hello\"); SI (x > 0) HAZ { IMPRIME (\"Positive\"); } SINO { IMPRIME (\"Negative\"); }";
        boolean result = parser.parse(code);
        System.out.println("Parsing result: " + result);
    }
}

*/