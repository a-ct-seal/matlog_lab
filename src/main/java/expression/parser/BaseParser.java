package expression.parser;

import expression.MathExpression;

public abstract class BaseParser {
    protected static final char END = '\0';
    protected StringSource source;
    protected char ch;

    public abstract MathExpression parse(String expression) throws Exception;

    public void setStream(StringSource source) {
        this.source = source;
        take();
    }

    protected void take() {
        ch = source.hasNext() ? source.next() : END;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }
}
