package ru.stepagin;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        Function system = new EquationSystem();
        Function.toCsv("./csv/FunctionOut/out.csv", system.countRange(new BigDecimal(-3), new BigDecimal(5), new BigDecimal("0.1")));

    }
}
