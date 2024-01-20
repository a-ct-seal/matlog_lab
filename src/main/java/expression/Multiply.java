package expression;

public class Multiply extends BinaryOperation {
    public Multiply(MathExpression leftOperand, MathExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String getOperatorToString() {
        return OperationStrings.Multiply;
    }

    @Override
    public NormalExpression normalize() {
        return leftOperand.normalize().multiply(rightOperand.normalize());
    }
}
