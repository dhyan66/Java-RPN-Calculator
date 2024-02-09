import java.util.*;
public class RPNCalculator {
    private Stack<Integer> valueStack; // intialising the stack

    RPNCalculator() { // constructor
        valueStack = new Stack<>();
    }

    public int evaluateExpression(String equation) { // method to evaluate the equation
        String[] chars = equation.split("\\s+"); // splitting the equation by chars and storing it inside the array
        for (String charac : chars) { // for loop for iterating through the array
            if (isNumber(charac)) { // checking for numbers
                valueStack.push(Integer.parseInt(charac));
            } else if (isOperator(charac)) { // checking for operators
                performOperation(charac);
            } else {
                throw new ArithmeticException("Invalid Operator"); //else return
            }
        }
        return valueStack.pop();
    }

    public String convertExpressionToRPN(String equation) { // method to convert equation to RPN
        StringBuilder rpnEquation = new StringBuilder();
        Stack<String> operatorStack = new Stack<>();

        String[] chars = equation.split("\\s+");
        for (String charac : chars) { // for loop for iterating through the array
            if (isNumber(charac)) { // checking for numbers
                rpnEquation.append(charac).append(" "); // appending the character to the string builder
            } else if (isOperator(charac)) { // checking for operators
                while (!operatorStack.isEmpty() && hasHigherPrecedence(operatorStack.peek(), charac)) { // checking if the stack is empty and checking for higher precedence
                    rpnEquation.append(operatorStack.pop()).append(" ");
                }
                operatorStack.push(charac);
            } else if ("(".equals(charac)) {  // checking for parenthesis
                operatorStack.push(charac);
            } else if (")".equals(charac)) {
                while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) { // checks if the operatorStack is not empty and the top of the stack is a parenthesis
                    rpnEquation.append(operatorStack.pop()).append(" "); // appends the top operator from the operatorStack to the rpnEquation StringBuilder and adds a space after it.
                }
                if (operatorStack.isEmpty() || !"(".equals(operatorStack.pop())) { // checking for unbalanced parenthesis
                    throw new ArithmeticException("Parenthesis are unbalanced!");
                }
            } else {
                throw new ArithmeticException("Invalid Operator");
            }
        }

        while (!operatorStack.isEmpty()) {
            if ("(".equals(operatorStack.peek())) {
                throw new ArithmeticException("Parenthesis are unbalanced!");
            }
            rpnEquation.append(operatorStack.pop()).append(" ");
        }

        return rpnEquation.toString().trim();
    }

    private boolean isNumber(String equation) { // method to check for numbers
        try {
            Integer.parseInt(equation);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) { // method to check if the character is an operator
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token) || "^".equals(token);
    }

    private void performOperation(String operator) {
        if (valueStack.size() < 2) {
            throw new ArithmeticException("Not enough operands in the stack");
        }

        int operand2 = valueStack.pop();
        int operand1 = valueStack.pop();

        switch (operator) { // switch case for checking the operator
            case "+":
                valueStack.push(operand1 + operand2);
                break;
            case "-":
                valueStack.push( operand1 - operand2);
                break;
            case "*":
                valueStack.push(operand1 * operand2);
                break;
            case "/":
                valueStack.push(operand1 / operand2);
                break;
            default:
                throw new ArithmeticException("Invalid Operator");
        }
    }

    private boolean hasHigherPrecedence(String op1, String op2) { // checking for higher precedence
        int precedenceOp1 = getPrecedence(op1);
        int precedenceOp2 = getPrecedence(op2);
        return precedenceOp1 > precedenceOp2;
    }

    private int getPrecedence(String operator) {
        switch (operator) {
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0; // Default precedence for invalid operators
        }
    }

}
