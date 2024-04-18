package ru.stepagin.logarithms;

import ru.stepagin.Function;

import java.math.BigDecimal;
import java.math.MathContext;

public class Ln extends Function {
    @Override
    public BigDecimal calc(BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        BigDecimal current = BigDecimal.ZERO;
        BigDecimal prev;
        int iter = 1;

        int MAX_ITERATIONS = 500000;
        if (x.subtract(BigDecimal.ONE).abs().compareTo(BigDecimal.ONE) <= 0) {
            do {
                prev = current;
                BigDecimal term = BigDecimal.valueOf(Math.pow(-1, iter - 1))
                        .multiply(BigDecimal.valueOf(Math.pow(x.subtract(BigDecimal.ONE).doubleValue(), iter)))
                        .divide(BigDecimal.valueOf(iter), MathContext.DECIMAL128);
                current = current.add(term);
                iter++;
            } while (
                    BigDecimal.valueOf(accuracy).compareTo(current.subtract(prev).abs()) <= 0
                            && iter < MAX_ITERATIONS
            );
        } else {
            do {
                prev = current;
                BigDecimal term = BigDecimal.valueOf(Math.pow(-1, iter - 1))
                        .multiply(BigDecimal.valueOf(Math.pow(x.subtract(BigDecimal.ONE).doubleValue(), -iter)))
                        .divide(BigDecimal.valueOf(iter), MathContext.DECIMAL128);
                current = current.add(term);
                iter++;
            } while (BigDecimal.valueOf(accuracy).compareTo(current.subtract(prev).abs()) <= 0 && iter < MAX_ITERATIONS);

            current = current.add(this.calc(x.subtract(BigDecimal.ONE)));
        }

        return current;
    }
}
