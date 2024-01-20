package expression;

import expression.parser.BaseParser;
import expression.parser.ExpressionParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split("=");
        BaseParser parser = new ExpressionParser();
        MathExpression arg1, arg2;
        try {
            arg1 = parser.parse(input[0]);
            arg2 = parser.parse(input[1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (arg1.normalize().equals(arg2.normalize())) {
            System.out.println("Равны");
        } else {
            System.out.println("Не равны");
        }
    }
}