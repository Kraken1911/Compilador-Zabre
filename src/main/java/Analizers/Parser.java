package Analizers;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Alexico.Token> tokens;
    private int currentTokenIndex;

    public Parser() {
        // Constructor vacío
    }

    public String parse(ArrayList<Alexico.Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        try {
            parseProgram();
            return "Expresión/es válida(s) \n";
        } catch (RuntimeException e) {
            return "Expresión no válida: " + e.getMessage();
        }
    }

    private Alexico.Token currentToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex);
        } else {
            throw new RuntimeException("Fin de los tokens inesperado.");
        }
    }

    private void advanceToken() {
        currentTokenIndex++;
    }

    private boolean match(Alexico.TokenType type) {
        if (currentToken().type == type) {
            advanceToken();
            return true;
        }
        return false;
    }

    private void parseProgram() {
        while (currentTokenIndex < tokens.size()) {
            parseDeclaration();
        }
    }

    private void parseDeclaration() {
        Alexico.Token token = currentToken();
        switch (token.type) {
            case ENT:
            case BOOL:
                parseVariableDeclaration();
                break;
            case IDENTIFIER:
                parseAssignmentDeclaration();
                break;
            default:
                throw new RuntimeException("Token inesperado: " + token.value);
        }
    }

    private void parseVariableDeclaration() {
        if (!match(Alexico.TokenType.ENT) && !match(Alexico.TokenType.BOOL)) {
            throw new RuntimeException("Se esperaba un tipo de dato.");
        }
        if (!match(Alexico.TokenType.IDENTIFIER)) {
            throw new RuntimeException("Se esperaba un identificador.");
        }
        if (!match(Alexico.TokenType.ASSIGN_OP)) {
            throw new RuntimeException("Se esperaba un operador de asignación '=='.");
        }
        parseExpression();
        if (!match(Alexico.TokenType.SEMICOLON)) {
            throw new RuntimeException("Se esperaba ';'.");
        }
    }

    private void parseAssignmentDeclaration() {
        if (!match(Alexico.TokenType.IDENTIFIER)) {
            throw new RuntimeException("Se esperaba un identificador.");
        }
        if (!match(Alexico.TokenType.ASSIGN_OP)) {
            throw new RuntimeException("Se esperaba un operador de asignación '=='.");
        }
        parseExpression();
        if (!match(Alexico.TokenType.SEMICOLON)) {
            throw new RuntimeException("Se esperaba ';'.");
        }
    }

    private void parseExpression() {
        parseTerm();
        while (currentToken().type == Alexico.TokenType.ARITHMETIC_OP) {
            advanceToken();
            parseTerm();
        }
    }

    private void parseTerm() {
        Alexico.Token token = currentToken();
        switch (token.type) {
            case NUMBER:
            case IDENTIFIER:
            case BOOL: // BOOL en vez de BOOL_LITERAL
                advanceToken();
                break;
            default:
                throw new RuntimeException("Se esperaba un número, identificador o literal booleano.");
        }
    }
}
