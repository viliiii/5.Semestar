package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;


/**
 * Class that uses ObjectStack to implement simple command line
 * calculator that parses one command line argument and returns the result
 * of the given numbers and operations.
 * Example 1: “8 2 /” means apply / on 8 and 2, so 8/2=4.
 * Example 2: “-1 8 2 / +” means apply / on 8 and 2, so 8/2=4,
 * then apply + on -1 and 4, so the result is 3.
 */
public class StackDemo {
    public static void main(String[] args) {
        String expression = args[0];

        String[] split = expression.split("\\s+");

        ObjectStack stack = new ObjectStack();
        for (String elem : split) {
            try {
                int number = Integer.parseInt(elem);
                stack.push(number);
            } catch (NumberFormatException ex) {
                int operator2;
                int operator1;

                switch (elem) {
                    case "+":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        stack.push(operator1 + operator2);
                        break;
                    case "-":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        stack.push(operator1 - operator2);
                        break;
                    case "/":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        if (operator2 == 0) {
                            System.out.println("You can not divide by zero.");
                            System.exit(0);
                        } else {
                            stack.push(operator1 / operator2);
                        }
                        break;
                    case "%":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        stack.push(operator1 % operator2);
                        break;
                    case "*":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        stack.push(operator1 * operator2);
                        break;
                    case "sqr":
                        operator1 = (int) stack.pop();
                        stack.push((int)Math.pow(operator1, 2));
                        break;
                    case "pow":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        stack.push((int)Math.pow(operator1, operator2));
                        break;
                    case "cubed":
                        operator1 = (int) stack.pop();
                        stack.push((int)Math.pow(operator1,3));
                        break;
                    case "bigger":
                        operator2 = (int) stack.pop();
                        operator1 = (int) stack.pop();
                        stack.push(Math.max(operator1, operator2));
                        break;
                }
            }
        }
        if (stack.size() != 1) {
            System.out.println("Error. Expression is invalid.");
        } else {
            System.out.println(stack.pop());
        }
    }
}
