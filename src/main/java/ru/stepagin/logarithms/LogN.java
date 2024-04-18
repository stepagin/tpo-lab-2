package ru.stepagin.logarithms;

import lombok.Getter;
import lombok.Setter;
import ru.stepagin.Function;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class LogN extends Function {
    private BigDecimal base;
    private Ln ln;

    public LogN(BigDecimal base, Ln ln) {
        if (base.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("основание логарифма должно быть положительным числом");
        if (base.equals(BigDecimal.ONE))
            throw new IllegalArgumentException("основание логарифма не должно быть равно 1");
        this.base = base;
        this.ln = ln;
    }

    public LogN(BigDecimal base) {
        this(base, new Ln());
    }

    public LogN(double base) {
        this(new BigDecimal(base), new Ln());
    }

    @Override
    public BigDecimal calc(BigDecimal x) {
        BigDecimal term1 = ln.calc(this.base);
        if (term1 == null)
            return null;
        BigDecimal term2 = ln.calc(x);
        if (term2 == null)
            return null;

        return term2.setScale(10, RoundingMode.HALF_EVEN).divide(term1.setScale(10, RoundingMode.HALF_EVEN), RoundingMode.HALF_EVEN);
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }
}
