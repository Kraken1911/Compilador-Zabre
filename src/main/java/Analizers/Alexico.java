package Analizers;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alexico {
    /**
 * Analizador Léxico
 *
 * Este programa es un analizador léxico que identifica y clasifica tokens en un código fuente
 * de acuerdo con las reglas definidas en el enumerado TokenType.
 *
 * Uso:
 * 1. Ejecuta el programa.
 * 2. Ingresa el código fuente cuando se te solicite.
 * 3. El programa analizará el código fuente y mostrará los tokens encontrados, junto con su tipo y valor.
 *
 * Ejemplo de entrada:
 * ENTRADA(x);
 * ENT y = 10;
 * IMPRIME(y);
 *
 * Salida esperada:
 * Tokens encontrados:
 * (ENTRADA, ENTRADA(x))
 * (ENT, ENT y = 10)
 * (IMPRIME, IMPRIME(y))
 *
 * Nota: El programa solo reconoce los tokens definidos en el enumerado TokenType.
 * Cualquier otro carácter o secuencia de caracteres no reconocida generará una excepción.
 */


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el código fuente: ");
        String input = scanner.nextLine();

        ArrayList<Token> tokens = getTokens(input);

        System.out.println("\nTokens encontrados:");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    private static ArrayList<Token> getTokens(String input) {
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
                throw new IllegalArgumentException("Caracter inválido en la posición " + start);
            }
        }
        return tokens;
    }

    private enum TokenType {
        IDENTIFIER("\\b[a-zA-Z][a-zA-Z0-9]*\\s*;"),
        ENTRADA("\\b(ENTRADA|entrada)\\s*\\(\\s*([a-zA-Z][a-zA-Z0-9]*)\\s*\\)\\s*;"),
        RETORNA("\\b(RETORNA|retorna)\\s*\\(\\s*([a-zA-Z0-9\\s+\\-\\*/]*)\\s*\\)\\s*;"),
        IMPRIME("\\b(IMPRIME|imprime)\\s*\\(\\s*([a-zA-Z0-9\\s+\\-\\*/\"']*)\\s*\\)\\s*;"),
        SI("\\b(SI|si)\\s*\\(\\s*([a-zA-Z][a-zA-Z0-9]*)\\s*\\)\\s*\\b(HAZ|haz)\\b\\s*\\{([a-zA-Z0-9\\s]*)\\}\\s*;"),
        SINO("\\b(SINO|sino)\\s*\\{([a-zA-Z0-9\\s]*)\\}\\s*;"),
        CAD("\\b(CAD|cad)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*\"[^\"]*\")\\s*;"),
        ENT("\\b(ENT|ent)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*\\d+)\\s*;"),
        BOOL("\\b(BOOL|bool)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*(true|false))\\s*;"),
        DEC("\\b(DEC|dec)\\s*([a-zA-Z][a-zA-Z0-9]*\\s*=\\s*\\d+\\.\\d+)\\s*;");
        private final Pattern pattern;

        TokenType(String regex) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    private static class Token {
        private final TokenType type;
        private final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + type + ", " + value + ")";
        }
    }
}
