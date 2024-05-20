package Analizers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Interprete {
    private Map<String, Object> variables = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public void execute(ArrayList<Alexico.Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            Alexico.Token token = tokens.get(i);
            switch (token.type) {
                case ENTRADA:
                    handleEntrada(token.value);
                    break;
                case IMPRIME:
                    handleImprime(token.value);
                    break;
                case SI:
                    i = handleSi(tokens, i);
                    break;
                case BUCLE:
                    i = handleBucle(tokens, i);
                    break;
                case CADENA:
                case ENT:
                case BOOL:
                case DEC:
                case IDENTIFIER:
                case NUMBER:
                    handleVariableAssignment(token);
                    break;
                case ARITHMETIC_OP:
                    handleArithmeticOperation(token.value);
                    break;
                case SEMICOLON:
                    // nada xD
                    break;
                default:
                    System.err.println("Token no reconocido: " + token.value);
                    break;
            }
        }
    }

    private void handleEntrada(String value) {
        String varName = value.split("\\(")[1].split("\\)")[0].trim();
        System.out.print("Introduce el valor para " + varName + ": ");
        String input = scanner.nextLine();
        variables.put(varName, input);
    }

    private void handleImprime(String value) {
        String content = value.split("\\(")[1].split("\\)")[0].trim();
        if (content.startsWith("\"") && content.endsWith("\"")) {
            System.out.println(content.substring(1, content.length() - 1));
        } else if (variables.containsKey(content)) {
            System.out.println(variables.get(content));
        } else {
            System.out.println("Variable no definida: " + content);
        }
    }

    private int handleSi(ArrayList<Alexico.Token> tokens, int currentIndex) {
        Alexico.Token token = tokens.get(currentIndex);
        String condition = token.value.split("\\(")[1].split("\\)")[0].trim();
        int endIndex = findEndOfBlock(tokens, currentIndex + 1);
        
        if (evaluateCondition(condition)) {
            execute(new ArrayList<>(tokens.subList(currentIndex + 2, endIndex)));
        } else if (endIndex + 1 < tokens.size() && tokens.get(endIndex + 1).type == Alexico.TokenType.SINO) {
            int elseEndIndex = findEndOfBlock(tokens, endIndex + 2);
            execute(new ArrayList<>(tokens.subList(endIndex + 2, elseEndIndex)));
            return elseEndIndex;
        }

        return endIndex;
    }

    private int handleBucle(ArrayList<Alexico.Token> tokens, int currentIndex) {
        Alexico.Token token = tokens.get(currentIndex);
        String[] parts = token.value.split("\\(")[1].split("\\)")[0].split(",");
        String init = parts[0].trim();
        String condition = parts[1].trim();
        String update = parts[2].trim();

        execute(new ArrayList<>(List.of(new Alexico.Token(Alexico.TokenType.IDENTIFIER, init))));

        int endIndex = findEndOfBlock(tokens, currentIndex + 1);
        while (evaluateCondition(condition)) {
            execute(new ArrayList<>(tokens.subList(currentIndex + 2, endIndex)));
            execute(new ArrayList<>(List.of(new Alexico.Token(Alexico.TokenType.IDENTIFIER, update))));
        }

        return endIndex;
    }

    private void handleVariableAssignment(Alexico.Token token) {
        String value = token.value;
        String[] parts = value.split("=");
        if (parts.length < 2) {
            System.err.println("Asignación mal formada: " + value);
            return;
        }

        String[] varNameParts = parts[0].trim().split(" ");
        if (varNameParts.length < 2) {
            System.err.println("Nombre de variable mal formado: " + parts[0].trim());
            return;
        }
        
        String varName = varNameParts[1];
        String varValue = parts[1].trim().replace(";", "");

        switch (token.type) {
            case CADENA:
                variables.put(varName, varValue.replace("\"", ""));
                break;
            case ENT:
                variables.put(varName, Integer.parseInt(varValue));
                break;
            case BOOL:
                variables.put(varName, Boolean.parseBoolean(varValue));
                break;
            case DEC:
                variables.put(varName, Double.parseDouble(varValue));
                break;
            case IDENTIFIER:
                if (varValue.matches("\\d+")) {
                    variables.put(varName, Integer.parseInt(varValue));
                } else if (varValue.matches("\\d+\\.\\d+")) {
                    variables.put(varName, Double.parseDouble(varValue));
                } else {
                    variables.put(varName, varValue);
                }
                break;
            case NUMBER:
                variables.put(varName, Double.parseDouble(varValue));
                break;
            default:
                System.err.println("Tipo de variable no reconocido: " + token.type);
                break;
        }
    }

    private void handleArithmeticOperation(String value) {
        String[] parts = value.split("[+\\-*/]");
        if (parts.length < 2) {
            System.err.println("Operación aritmética mal formada: " + value);
            return;
        }

        String var1 = parts[0].trim();
        String var2 = parts[1].trim().replace(";", "");
        char operator = value.replaceAll("\\s+", "").charAt(var1.length());

        Object result = null;
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (variables.get(var1) instanceof Integer && variables.get(var2) instanceof Integer) {
                int int1 = (int) variables.get(var1);
                int int2 = (int) variables.get(var2);
                result = switch (operator) {
                    case '+' -> int1 + int2;
                    case '-' -> int1 - int2;
                    case '*' -> int1 * int2;
                    case '/' -> int1 / int2;
                    default -> null;
                };
            } else if (variables.get(var1) instanceof Double && variables.get(var2) instanceof Double) {
                double double1 = (double) variables.get(var1);
                double double2 = (double) variables.get(var2);
                result = switch (operator) {
                    case '+' -> double1 + double2;
                    case '-' -> double1 - double2;
                    case '*' -> double1 * double2;
                    case '/' -> double1 / double2;
                    default -> null;
                };
            } else if (variables.get(var1) instanceof Integer && variables.get(var2) instanceof Double) {
                double double1 = ((Integer) variables.get(var1)).doubleValue();
                double double2 = (double) variables.get(var2);
                result = switch (operator) {
                    case '+' -> double1 + double2;
                    case '-' -> double1 - double2;
                    case '*' -> double1 * double2;
                    case '/' -> double1 / double2;
                    default -> null;
                };
            } else if (variables.get(var1) instanceof Double && variables.get(var2) instanceof Integer) {
                double double1 = (double) variables.get(var1);
                double double2 = ((Integer) variables.get(var2)).doubleValue();
                result = switch (operator) {
                    case '+' -> double1 + double2;
                    case '-' -> double1 - double2;
                    case '*' -> double1 * double2;
                    case '/' -> double1 / double2;
                    default -> null;
                };
            }
        }

        if (result != null) {
            System.out.println("Resultado de la operación: " + result);
        } else {
            System.err.println("Error en la operación aritmética: " + value);
        }
    }

    private boolean evaluateCondition(String condition) {
        String[] parts = condition.split(" ");
        if (parts.length < 3) {
            System.err.println("Condición mal formada: " + condition);
            return false;
        }

        String var1 = parts[0].trim();
        String operator = parts[1].trim();
        String var2 = parts[2].trim();

        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            Object value1 = variables.get(var1);
            Object value2 = variables.get(var2);

            if (value1 instanceof Integer && value2 instanceof Integer) {
                int int1 = (int) value1;
                int int2 = (int) value2;
                return switch (operator) {
                    case ">" -> int1 > int2;
                    case "<" -> int1 < int2;
                    case "==" -> int1 == int2;
                    case "!=" -> int1 != int2;
                    case ">=" -> int1 >= int2;
                    case "<=" -> int1 <= int2;
                    default -> false;
                };
            } else if (value1 instanceof Double && value2 instanceof Double) {
                double double1 = (double) value1;
                double double2 = (double) value2;
                return switch (operator) {
                    case ">" -> double1 > double2;
                    case "<" -> double1 < double2;
                    case "==" -> double1 == double2;
                    case "!=" -> double1 != double2;
                    case ">=" -> double1 >= double2;
                    case "<=" -> double1 <= double2;
                    default -> false;
                };
            }
        }

        System.err.println("Variables no definidas o tipos incompatibles en la condición: " + condition);
        return false;
    }

    private int findEndOfBlock(ArrayList<Alexico.Token> tokens, int startIndex) {
        int braceCount = 0;
        for (int i = startIndex; i < tokens.size(); i++) {
            if (tokens.get(i).type == Alexico.TokenType.LBRACE) {
                braceCount++;
            } else if (tokens.get(i).type == Alexico.TokenType.RBRACE) {
                if (braceCount == 0) {
                    return i;
                }
                braceCount--;
            }
        }
        return tokens.size() - 1; 
    }
}
