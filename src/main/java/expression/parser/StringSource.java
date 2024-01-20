package expression.parser;

public class StringSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    public boolean hasNext() {
        return pos < data.length();
    }

    public char next() {
        return data.charAt(pos++);
    }

    public int getPosition() {
        return pos;
    }

    public void setPosition(int newPos) {
        pos = newPos;
    }
}
