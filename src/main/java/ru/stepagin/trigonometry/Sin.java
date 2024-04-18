package ru.stepagin.trigonometry;

import ru.stepagin.Function;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Sin extends Function {
    @Override
    public BigDecimal calc(BigDecimal x) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal prev;
        int i = 0;

        do {
            prev = sum;
            sum = sum.add(BigDecimal.valueOf(minusOnePower(i)).multiply(prod(x, 2 * i + 1)));
            ++i;
        } while (BigDecimal.valueOf(accuracy).compareTo(prev.subtract(sum).abs()) <= 0);

        return sum;
    }

    public static int minusOnePower(int n) {
        return (int) Math.pow(-1, n);
    }

    public static BigDecimal prod(BigDecimal x, int n) {
        BigDecimal buf = BigDecimal.ONE.setScale(10, RoundingMode.HALF_EVEN);

        for (int i = 1; i <= n; i++) {
            buf = buf.multiply(x.divide(BigDecimal.valueOf(i), MathContext.DECIMAL128));
        }

        return buf;
    }
}
