package ru.stepagin;

import lombok.Getter;
import lombok.Setter;
import ru.stepagin.logarithms.Ln;
import ru.stepagin.logarithms.LogN;
import ru.stepagin.trigonometry.Sec;
import ru.stepagin.trigonometry.Sin;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class EquationSystem extends Function {
    private Sec sec;
    private Sin sin;
    private Ln ln;
    private LogN log3;
    private LogN log5;
    private final int compAcc = 5; // comparison accuracy in decimals

    public EquationSystem() {
        this.sec = new Sec();
        this.sin = new Sin();
        this.ln = new Ln();
        this.log3 = new LogN(new BigDecimal(3));
        this.log5 = new LogN(new BigDecimal(5));
    }

    @Override
    public BigDecimal calc(BigDecimal x) {
        x = BigDecimal.valueOf(x.doubleValue());
        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            // (sin(x) * sec(x))
            BigDecimal term1 = this.sec.calc(x);
            if (term1 == null)
                return null;
            return this.sin.calc(x).multiply(this.sec.calc(x));
//            return new BigDecimal(Math.tan(x.doubleValue()));
        } else {
            // equation: (((((log_5(x) ^ 2) * ln(x)) / log_3(x)) * ((log_5(x) ^ 2) - log_5(x))) + (log_3(x) ^ 3))
            // term1: (( (log_5(x) ^ 2) * ln(x) ) / log_3(x))
            // term2: ((log_5(x) ^ 2) - log_5(x))
            // term3: ((term1 * term2) + (log_3(x) ^ 3))
            if (x.compareTo(BigDecimal.ONE) == 0)
                return BigDecimal.ZERO;
            BigDecimal term1 = (log5.calc(x).setScale(10, RoundingMode.HALF_EVEN)
                    .pow(2)
                    .multiply(ln.calc(x)))
                    .divide(log3.calc(x),
                            RoundingMode.HALF_EVEN);
            BigDecimal term2 = (log5.calc(x).pow(2)).subtract(log5.calc(x));
            BigDecimal term3 = (((term1).multiply(term2)).add(log3.calc(x).pow(3)));
            return term3;
        }
    }

}
