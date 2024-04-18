package ru.stepagin.trigonometry;

import lombok.Getter;
import lombok.Setter;
import ru.stepagin.Function;

import java.math.BigDecimal;

@Getter
@Setter
public class Cos extends Function {
    private Sin sin;

    public Cos() {
        this.sin = new Sin();
    }


    @Override
    public BigDecimal calc(BigDecimal x) {
        return sin.calc(x.add(new BigDecimal(Math.PI / 2)));
    }
}
