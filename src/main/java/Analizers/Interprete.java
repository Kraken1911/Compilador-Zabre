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
                    i = handleSi(token.value, tokens, i);
                    break;
                case CAD:
                case ENT:
                case BOOL:
                case DEC:
                    handleVariableAssignment(token.value);
                    break;
                case ARITHMETIC_OP:
                    handleArithmeticOperation(token.value);
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
    } else {
        // Evaluar la expresión aritmética
        String[] parts = content.split("(?=[-+*/])");
        List<Integer> values = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        for (String part : parts) {
            part = part.trim();
            if (part.matches("\\d+")) {
                values.add(Integer.parseInt(part));
            } else if (part.matches("[+\\-*/]")) {
                operators.add(part.charAt(0));
            } else if (variables.containsKey(part)) {
                values.add((Integer) variables.get(part));
            } else {
                System.out.println("Variable no definida: " + part);
                return;
            }
        }

        int result = values.get(0);
        for (int i = 0; i < operators.size(); i++) {
            char operator = operators.get(i);
            int operand = values.get(i + 1);
            result = switch (operator) {
                case '+' -> result + operand;
                case '-' -> result - operand;
                case '*' -> result * operand;
                case '/' -> result / operand;
                default -> 0;
            };
        }
        System.out.println("Resultado: " + result);
    }
}


    private int handleSi(String value, ArrayList<Alexico.Token> tokens, int currentIndex) {
        String condition = value.split("\\(")[1].split("\\)")[0].trim();
        String trueBlock = value.split("\\{")[1].split("\\}")[0].trim();
        String falseBlock = null;

        // Buscar bloque SINO
        for (int i = currentIndex + 1; i < tokens.size(); i++) {
            if (tokens.get(i).type == Alexico.TokenType.SINO) {
                falseBlock = tokens.get(i).value.split("\\{")[1].split("\\}")[0].trim();
                currentIndex = i;  // Actualizar el índice para saltar el bloque SINO
                break;
            }
        }

        if (evaluateCondition(condition)) {
            execute(new Alexico().getTokens(trueBlock));
        } else if (falseBlock != null) {
            execute(new Alexico().getTokens(falseBlock));
        }

        return currentIndex;
    }

    private void handleVariableAssignment(String value) {
        String[] parts = value.split("=");
        String varName = parts[0].trim().split(" ")[1];
        String varValue = parts[1].trim().replace(";", "");

        if (value.startsWith("CAD")) {
            variables.put(varName, varValue.replace("\"", ""));
        } else if (value.startsWith("ENT")) {
            variables.put(varName, Integer.parseInt(varValue));
        } else if (value.startsWith("BOOL")) {
            variables.put(varName, Boolean.parseBoolean(varValue));
        } else if (value.startsWith("DEC")) {
            variables.put(varName, Double.parseDouble(varValue));
        }
    }

    private void handleArithmeticOperation(String value) {
        String[] parts = value.split("[+\\-*/]");
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
            // Asignar el resultado a una variable (si es necesario)
            // Aquí se puede agregar lógica para manejar la asignación del resultado
            System.out.println("Resultado de la operación: " + result);
        } else {
            System.err.println("Error en la operación aritmética: " + value);
        }
    }

    private boolean evaluateCondition(String condition) {
        // Evaluar la condición (esto es un ejemplo simplificado)
        String[] parts = condition.split(" ");
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

        return false;
    }
}
      
