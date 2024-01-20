package expression;

public class Degree extends BinaryOperation {
    public Degree(MathExpression leftOperand, MathExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String getOperatorToString() {
        return OperationStrings.Degree;
    }

    @Override
    public NormalExpression normalize() {
        return leftOperand.normalize().power(rightOperand.normalize());
    }
}
