package expression;

import java.util.ArrayList;
import java.util.List;

public class NormalExpression {

    private static class Term {
        private int number;
        private boolean hasPower = false;
        private NormalExpression power = null;

        public Term(int number) {
            this.number = number;
        }

        public Term(int number, NormalExpression power) {
            this.number = number;
            this.hasPower = true;
            this.power = power;
        }

        public Term(Term term) {
            this.number = term.number;
            if (term.hasPower) {
                this.hasPower = true;
                this.power = new NormalExpression(term.power);
            }
        }

        public NormalExpression getPower() {
            if (hasPower) {
                return power;
            }
            return new NormalExpression(0);
        }

        public int compare(Term right) {
            // 1  - bigger
            // 0  - equals
            // -1 - smaller
            if (hasPower && !right.hasPower) {
                return 1;
            }
            if (!hasPower && right.hasPower) {
                return -1;
            }
            if (!hasPower) {
                return Integer.compare(number, right.number);
            }
            int powerCmp = getPower().compare(right.getPower());
            int numberCmp = Integer.compare(number, right.number);
            return powerCmp == 0 ? numberCmp : powerCmp;
        }
    }

    public List<Term> ordinal;

    public NormalExpression(int n) {
        ordinal = new ArrayList<>(List.of(new Term(n)));
    }

    public NormalExpression() {
        ordinal = new ArrayList<>(List.of(new Term(1, new NormalExpression(1))));
    }

    public NormalExpression(NormalExpression other) {
        this(other.ordinal);
    }

    private NormalExpression(List<Term> ordinal) {
        this.ordinal = new ArrayList<>();
        for (Term term : ordinal) {
            this.ordinal.add(new Term(term));
        }
    }

    public NormalExpression add(NormalExpression right) {
        if (this == right) {
            ordinal.get(ordinal.size() - 1).number *= 2;
            return this;
        }
        int cmp = ordinal.get(ordinal.size() - 1).getPower().compare(right.ordinal.get(0).getPower());
        while (cmp == -1) {
            ordinal.remove(ordinal.size() - 1);
            if (ordinal.isEmpty()) {
                break;
            }
            cmp = ordinal.get(ordinal.size() - 1).getPower().compare(right.ordinal.get(0).getPower());
        }
        if (cmp == 0 && !ordinal.isEmpty()) {
            ordinal.get(ordinal.size() - 1).number += right.ordinal.get(0).number;
            ordinal.addAll(right.ordinal.subList(1, right.ordinal.size()));
        } else {
            ordinal.addAll(right.ordinal);
        }
        return this;
    }

    public NormalExpression multiply(NormalExpression right) {
        NormalExpression zero = new NormalExpression(0);
        if (compare(zero) == 0 || right.compare(zero) == 0) {
            ordinal = zero.ordinal;
            return this;
        }
        NormalExpression total = new NormalExpression(0);
        for (int i = 0; i < right.ordinal.size(); i++) {
            NormalExpression result;
            if (right.ordinal.get(i).getPower().compare(zero) == 0) {
                result = new NormalExpression(this);
                result.ordinal.get(0).number *= right.ordinal.get(i).number;
            } else {
                NormalExpression newPower = new NormalExpression(ordinal.get(0).getPower());
                newPower.add(right.ordinal.get(i).getPower());
                result = new NormalExpression(new ArrayList<>(List.of(new Term(right.ordinal.get(i).number, newPower))));
            }
            if (i == 0) {
                total.ordinal = new ArrayList<>(result.ordinal);
            } else {
                total.add(result);
            }
        }
        ordinal = total.ordinal;
        return this;
    }

    public NormalExpression power(NormalExpression right) {
        NormalExpression omega = new NormalExpression();
        NormalExpression total = new NormalExpression(1);
        if (compare(total) == 0 || compare(new NormalExpression(0)) == 0) {
            return this;
        }
        if (compare(omega) == -1) {
            if (right.compare(omega) == -1) {
                ordinal.get(0).number = (int) Math.pow(ordinal.get(0).number, right.ordinal.get(0).number);
                return this;
            }
            int a = ordinal.get(0).number;
            int m = 0;
            if (!right.ordinal.get(right.ordinal.size() - 1).hasPower) {
                m = right.ordinal.get(right.ordinal.size() - 1).number;
            }
            List<Term> newOrd = new ArrayList<>();
            for (int i = 0; i < right.ordinal.size(); i++) {
                if (right.ordinal.get(i).hasPower &&
                    right.ordinal.get(i).power.compare(omega) > -1) {
                    newOrd.add(new Term(right.ordinal.get(i)));
                } else if (right.ordinal.get(i).hasPower && right.ordinal.get(i).power.ordinal.get(0).number != 1) {
                    newOrd.add(new Term(right.ordinal.get(i)));
                    newOrd.get(newOrd.size() - 1).power.ordinal.get(0).number--;
                } else if (right.ordinal.get(i).hasPower) {
                    newOrd.add(new Term(right.ordinal.get(i).number));
                }
            }
            NormalExpression omegaPower = new NormalExpression();
            omegaPower.ordinal = newOrd;
            ordinal = new ArrayList<>(List.of(new Term(
                    (int) Math.pow(a, m),
                    omegaPower
            )));
            return this;
        }
        for (int i = 0; i < right.ordinal.size(); i++) {
            NormalExpression rightTerm = new NormalExpression(new ArrayList<>(List.of(right.ordinal.get(i))));
            NormalExpression result;
            if (rightTerm.compare(omega) > -1) {
                result = new NormalExpression(new ArrayList<>(List.of(
                        new Term(1, (new NormalExpression(ordinal.get(0).getPower())).multiply(rightTerm)))));
            } else {
                result = power(rightTerm.ordinal.get(0).number);
            }
            if (i == 0) {
                total.ordinal = new ArrayList<>(result.ordinal);
            } else {
                total.multiply(result);
            }
        }
        ordinal = total.ordinal;
        return this;
    }

    public boolean equals(NormalExpression right) {
        return compare(right) == 0;
    }

    private NormalExpression power(int pow) {
        if (pow == 0) {
            return new NormalExpression(1);
        }
        NormalExpression res = new NormalExpression(this);
        if (pow % 2 == 0) {
            return res.multiply(res).power(pow / 2);
        }
        return res.multiply(this.power(pow - 1));
    }

    private int compare(NormalExpression right) {
        // 1  - bigger
        // 0  - equals
        // -1 - smaller

        int i = -1;
        int cmp = 0;
        while (cmp == 0 && (i + 1) < ordinal.size() && (i + 1) < right.ordinal.size()) {
            i++;
            cmp = ordinal.get(i).compare(right.ordinal.get(i));
        }
        i++;
        if (i == ordinal.size() && i < right.ordinal.size()) {
            return -1;
        }
        if (i < ordinal.size() && i == right.ordinal.size()) {
            return 1;
        }
        return cmp;
    }
}
