package expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationPriority {
    public static final int UnaryOperation = 0;
    public static final int Operand = 0;
    public static final int Degree = 1;
    public static final int Multiply = 2;
    public static final int Add = 3;
    public static final int GreatestPriority;
    public static final Map<Integer, List<String>> priorities;

    static {
        GreatestPriority = max(UnaryOperation, Operand,
                Add, Multiply, Degree);
        priorities = new HashMap<>();
        for (int i = 0; i < OperationPriority.GreatestPriority; i++) {
            priorities.put(i + 1, new ArrayList<>());
        }
        priorities.get(Add).add(OperationStrings.Add);
        priorities.get(Multiply).add(OperationStrings.Multiply);
        priorities.get(Degree).add(OperationStrings.Degree);
    }

    private static int max(Integer... values) {
        int res = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > res) {
                res = values[i];
            }
        }
        return res;
    }
}
