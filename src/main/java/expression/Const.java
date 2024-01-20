package expression;

import java.util.Objects;

public class Const extends Operand {
    private final int number;

    public Const(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Const const1) {
            return Objects.equals(number, const1.number);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public NormalExpression normalize() {
        return new NormalExpression(number);
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
