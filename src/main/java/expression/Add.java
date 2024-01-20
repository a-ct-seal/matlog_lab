package expression;

public class Add extends BinaryOperation {
    public Add(MathExpression leftOperand, MathExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String getOperatorToString() {
        return OperationStrings.Add;
    }


    @Override
    public NormalExpression normalize() {
        return leftOperand.normalize().add(rightOperand.normalize());
    }
}
