package ru.stepagin.trigonometry;

import lombok.Getter;
import lombok.Setter;
import ru.stepagin.Function;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class Sec extends Function {
    private Cos cos;

    public Sec() {
        this.cos = new Cos();
    }

    @Override
    public BigDecimal calc(BigDecimal x) {
        BigDecimal term1 = this.cos.calc(x);
        if (term1.equals(BigDecimal.ZERO)) {
            return BigDecimal.valueOf(Double.NaN);
        }
        return new BigDecimal("1.0").setScale(10, RoundingMode.HALF_EVEN).divide(term1, RoundingMode.HALF_EVEN);
    }
}
