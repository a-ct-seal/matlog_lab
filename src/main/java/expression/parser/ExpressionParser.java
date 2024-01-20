package expression.parser;

import expression.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser extends BaseParser{
    private static MathExpression buildExpression(String exprName, MathExpression left, MathExpression right) {
        return switch (exprName) {
            case OperationStrings.Add -> new Add(left, right);
            case OperationStrings.Multiply -> new Multiply(left, right);
            case OperationStrings.Degree -> new Degree(left, right);
            default -> throw new AssertionError("Unreachable statement");
        };
    }

    private static boolean leftAssociative(String exprName) {
        return switch (exprName) {
            case OperationStrings.Add, OperationStrings.Multiply -> true;
            case OperationStrings.Degree -> false;
            default -> throw new AssertionError("Unreachable statement");
        };
    }

    @Override
    public MathExpression parse(String expression) {
        setStream(new StringSource(expression));
        return parseComplexObject(OperationPriority.GreatestPriority);
    }

    private MathExpression parseComplexObject(int priority) {
        if (priority == 0) {
            return parsePrimaryObject();
        }
        List<MathExpression> operations = new ArrayList<>();
        operations.add(parseComplexObject(priority - 1));
        String possibleOperation = OperationPriority.priorities.get(priority).get(0);
        while (takeOperator(possibleOperation)) {
            operations.add(parseComplexObject(priority - 1));
        }
        MathExpression result;
        if (leftAssociative(possibleOperation)) {
            result = operations.get(0);
            for (int i = 1; i < operations.size(); i++) {
                result = buildExpression(possibleOperation, result, operations.get(i));
            }
        } else {
            result = operations.get(operations.size() - 1);
            for (int i = operations.size() - 2; i >= 0; i--) {
                result = buildExpression(possibleOperation, operations.get(i), result);
            }
        }
        return result;
    }

    private MathExpression parsePrimaryObject() {
        skipWhitespaces();
        if (take('(')) {
            skipWhitespaces();
            MathExpression res = parseComplexObject(OperationPriority.GreatestPriority);
            take(')');
            skipWhitespaces();
            return res;
        } else {
            return getToken();
        }
    }

    private Operand getToken() {
        Operand res;
        if (Character.isDigit(ch)) {
            StringBuilder num = new StringBuilder();
            while (Character.isDigit(ch)) {
                num.append(ch);
                take();
            }
            res = new Const(Integer.parseInt(num.toString()));
        } else {
            StringBuilder var = new StringBuilder();
            while(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '$') {
                var.append(ch);
                take();
            }
            res = new Variable(var.toString());
        }
        skipWhitespaces();
        return res;
    }

    protected boolean takeOperator(final String expected) {
        int oldIdx = source.getPosition();
        char oldCh = ch;
        int idx = 0;
        while (idx < expected.length()) {
            if (take(expected.charAt(idx))) {
                idx++;
            } else {
                source.setPosition(oldIdx);
                ch = oldCh;
                return false;
            }
        }
        boolean isSymbolOperation = !(Character.isDigit(expected.charAt(expected.length() - 1)) ||
                Character.isLetter(expected.charAt(expected.length() - 1)));
        if (!(Character.isWhitespace(ch) || ch == '(' || ch == '-' ||
                (isSymbolOperation && (Character.isLetter(ch) || Character.isDigit(ch)))
        )) {
            source.setPosition(oldIdx);
            ch = oldCh;
            return false;
        }
        return true;
    }

    private void skipWhitespaces() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }
}
