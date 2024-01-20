package expression;

import java.util.Objects;

public abstract class BinaryOperation implements MathExpression {
    protected final MathExpression leftOperand;
    protected final MathExpression rightOperand;

    public BinaryOperation(MathExpression leftOperand, MathExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public abstract String getOperatorToString();

    @Override
    public String toString() {
        return "(" + leftOperand.toString() +
                " " + getOperatorToString() +
                " " + rightOperand.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass() &&
                Objects.equals(((BinaryOperation) obj).leftOperand, this.leftOperand) &&
                Objects.equals(((BinaryOperation) obj).rightOperand, this.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, rightOperand, getClass());
    }
}
