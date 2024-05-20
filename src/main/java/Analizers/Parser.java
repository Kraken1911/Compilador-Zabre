package Analizers;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Alexico.Token> tokens;
    private int currentTokenIndex;
    private int currentLine;
    public boolean verificado = false;

    public Parser() {
        // Constructor vacío
    }

    // Método principal para iniciar el análisis sintáctico
    public String parse(ArrayList<Alexico.Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.currentLine = 1;
        try {
            // Inicia el análisis del programa
            parseProgram();
            verificado = true; // Indica que el análisis fue exitoso
            return "Los tokens del arrayList son válidos.";
        } catch (RuntimeException e) {
            return e.getMessage(); // Devuelve el mensaje de error en caso de excepción
        }
    }

    // Obtiene el token actual
    private Alexico.Token currentToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex);
        } else {
            throw new RuntimeException("Error: Se esperaba un token, pero se encontró el fin de los tokens en la línea " + currentLine + ".");
        }
    }

    // Avanza al siguiente token y actualiza la línea si se encuentra un punto y coma
    private void advanceToken() {
        if (currentToken().type == Alexico.TokenType.SEMICOLON) {
            currentLine++;
        }
        currentTokenIndex++;
    }

    // Verifica si el token actual coincide con el tipo esperado y avanza
    private boolean match(Alexico.TokenType type) {
        if (currentToken().type == type) {
            advanceToken();
            return true;
        }
        return false;
    }

    // Analiza la estructura de un programa
    private void parseProgram() {
        // Continúa analizando declaraciones mientras haya tokens disponibles
        while (currentTokenIndex < tokens.size()) {
            parseDeclaration();
        }
    }

    // Analiza una declaración (de variable, condicional, bucle, etc.)
    private void parseDeclaration() {
        Alexico.Token token = currentToken();
        switch (token.type) {
            case IDENTIFIER:
                parseVariableDeclaration();
                break;
            case SI:
                parseIfStatement();
                break;
            case BUCLE:
                parseLoopStatement();
                break;
            case IMPRIME:
                parsePrintStatement();
                break;
            case RETORNA:
                parseReturnStatement();
                break;
            default:
                throw new RuntimeException("Error: Token inesperado en declaración: " + token.value + " en la línea " + currentLine + ".");
        }
    }

    // Analiza una declaración de variable
    private void parseVariableDeclaration() {
        if (!match(Alexico.TokenType.IDENTIFIER)) {
            throw new RuntimeException("Error: Se esperaba un tipo de dato en la línea " + currentLine + ".");
        }
        if (!match(Alexico.TokenType.VARIABLE)) {
            throw new RuntimeException("Error: Se esperaba un identificador en la línea " + currentLine + ".");
        }
        if (!match(Alexico.TokenType.ASSIGN_OP)) {
            throw new RuntimeException("Error: Se esperaba un operador de asignación '==' en la línea " + currentLine + ".");
        }
        parseExpression();
        if (!match(Alexico.TokenType.SEMICOLON)) {
            throw new RuntimeException("Error: Se esperaba ';' en la línea " + currentLine + ".");
        }
    }

    // Analiza una expresión
    private void parseExpression() {
        parseTerm();
        while (currentToken().type == Alexico.TokenType.ARITHMETIC_OP) {
            advanceToken();
            parseTerm();
        }
    }

    // Analiza un término (número, variable, literal booleano)
    private void parseTerm() {
        Alexico.Token token = currentToken();
        switch (token.type) {
            case NUMBER:
            case VARIABLE:
            case BOOLEAN_LITERAL:
                advanceToken();
                break;
            case WRONG_ASSIGNMENT_BOOLEAN:
                throw new RuntimeException("Error: Valor erróneo de asignamiento en la línea " + currentLine + ".");
            default:
                throw new RuntimeException("Error: Se esperaba un número, identificador o literal booleano, encontrado: " + token.value + " en la línea " + currentLine + ".");
        }
    }

    // Analiza una declaración 'SI' (if)
    private void parseIfStatement() {
        if (!match(Alexico.TokenType.SI)) {
            throw new RuntimeException("Error: Se esperaba 'SI' en la línea " + currentLine + ".");
        }
        if (!match(Alexico.TokenType.LPAREN)) {
            throw new RuntimeException("Error: Se esperaba '(' en la línea " + currentLine + ".");
        }
        parseExpression();
        if (!match(Alexico.TokenType.RPAREN)) {
            throw new RuntimeException("Error: Se esperaba ')' en la línea " + currentLine + ".");
        }
        if (!match(Alexico.TokenType.LBRACE)) {
            throw new RuntimeException("Error: Se esperaba '{' en la línea " + currentLine + ".");
        }
        parseProgram();
        if (!match(Alexico.TokenType.RBRACE)) {
            throw new RuntimeException("Error: Se esperaba '}' en la línea " + currentLine + ".");
        }
        if (match(Alexico.TokenType.SINO)) {
            if (!match(Alexico.TokenType.LBRACE)) {
                throw new RuntimeException("Error: Se esperaba '{' en la línea " + currentLine + ".");
            }
            parseProgram();
            if (!match(Alexico.TokenType.RBRACE)) {
                throw new RuntimeException("Error: Se esperaba '}' en la línea " + currentLine + ".");
            }
        }
    }

    // Analiza una declaración 'BUCLE' (loop)
    private void parseLoopStatement() {
        if (!match(Alexico.TokenType.BUCLE)) {
            throw new RuntimeException("Error: Se esperaba 'BUCLE' en la línea " + currentLine + ".");
        }
        if (!match(Alexico.TokenType.LPAREN)) {
            throw new RuntimeException("Error: Se esperaba '(' en la línea " + currentLine + ".");
        }
        parseExpression();
        if (!match(Alexico.TokenType.RPAREN)) {
            throw new RuntimeException("Error: Se esperaba ')' en la línea " + currentLine + ".");
        }
        if (!match(Alexico.TokenType.LBRACE)) {
            throw new RuntimeException("Error: Se esperaba '{' en la línea " + currentLine + ".");
        }
        parseProgram();
        if (!match(Alexico.TokenType.RBRACE)) {
            throw new RuntimeException("Error: Se esperaba '}' en la línea " + currentLine + ".");
        }
    }

    // Analiza una declaración 'IMPRIME' (print)
    private void parsePrintStatement() {
        if (!match(Alexico.TokenType.IMPRIME)) {
            throw new RuntimeException("Error: Se esperaba 'IMPRIME' en la línea " + currentLine + ".");
        }
        parseExpression();
        if (!match(Alexico.TokenType.SEMICOLON)) {
            throw new RuntimeException("Error: Se esperaba ';' en la línea " + currentLine + ".");
        }
    }

    // Analiza una declaración 'RETORNA' (return)
    private void parseReturnStatement() {
        if (!match(Alexico.TokenType.RETORNA)) {
            throw new RuntimeException("Error: Se esperaba 'RETORNA' en la línea " + currentLine + ".");
        }
        parseExpression();
        if (!match(Alexico.TokenType.SEMICOLON)) {
            throw new RuntimeException("Error: Se esperaba ';' en la línea " + currentLine + ".");
        }
    }

    // Genera el árbol sintáctico para una línea específica
    public String creacionArbol(boolean verificación, int numLinea, ArrayList<Alexico.Token> tokens) {
        if (!verificación) {
            return "No se puede crear el árbol sintáctico.";
        }

        // Contar la cantidad de líneas basándonos en el token SEMICOLON
        int lineaActual = 1;
        int inicioTokensLinea = 0;

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).type == Alexico.TokenType.SEMICOLON) {
                if (lineaActual == numLinea) {
                    // Extraer los tokens correspondientes a la línea especificada
                    List<Alexico.Token> lineaTokens = tokens.subList(inicioTokensLinea, i + 1);
                    return generarArbol(lineaTokens);
                }
                lineaActual++;
                inicioTokensLinea = i + 1;
            }
        }

        // Si se llega aquí, significa que la línea especificada no existe
        return "No existe dicha línea indicada.";
    }

    // Genera una representación en forma de árbol de la lista de tokens proporcionada
    private String generarArbol(List<Alexico.Token> tokensLinea) {
        // Inicializar el árbol sintáctico como una cadena
        StringBuilder arbol = new StringBuilder();
        arbol.append("Arbol Sintáctico:\n");

        // Añadir la estructura del árbol basándose en los tokens
        for (Alexico.Token token : tokensLinea) {
            arbol.append(token.type).append(" (").append(token.value).append(")\n");
        }

        return arbol.toString();
    }

    // Convierte el árbol sintáctico a formato DOT para visualización gráfica
    public String convertirArbolAFormatoVisual(String arbol) {
        String[] lineas = arbol.split("\n");
        StringBuilder dot = new StringBuilder();

        dot.append("digraph G {\n");
        dot.append("node [shape=box];\n");

        // Lista para mantener los nodos y conexiones
        ArrayList<String> nodos = new ArrayList<>();
        ArrayList<String> conexiones = new ArrayList<>();

        // Para la construcción jerárquica del árbol
        int nodeCounter = 0;
        String parentNode = "node" + nodeCounter++;
        dot.append(parentNode).append(" [label=\"Statement\"];\n");
        nodos.add(parentNode);

        String currentParent = parentNode;

        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;

            String nodo = "node" + nodeCounter++;
            String etiqueta = linea.contains(" (") ? linea.substring(0, linea.indexOf(" (")) : linea;

            switch (etiqueta) {
                case "=":
                case "==":
                case "+":
                case "-":
                case "*":
                case "/":
                case ";":
                    nodos.add(nodo);
                    dot.append(nodo).append(" [label=\"").append(etiqueta).append("\"];\n");
                    conexiones.add(currentParent + " -> " + nodo);
                    currentParent = nodo;
                    break;
                default:
                    nodos.add(nodo);
                    dot.append(nodo).append(" [label=\"").append(etiqueta).append("\"];\n");
                    conexiones.add(currentParent + " -> " + nodo);
                    break;
            }
        }

        for (String conexion : conexiones) {
            dot.append(conexion).append(";\n");
        }

        dot.append("}");
        return dot.toString();
    }
}
