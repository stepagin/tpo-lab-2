package ru.stepagin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class Function {
    public abstract BigDecimal calc(BigDecimal x);

    public final double accuracy = 0.00001;

    public final SortedMap<BigDecimal, BigDecimal> countRange(BigDecimal start, BigDecimal finish, BigDecimal step) {
        SortedMap<BigDecimal, BigDecimal> result = new TreeMap<>();
        for (BigDecimal i = start; i.compareTo(finish) <= 0; i = i.add(step))
            result.put(i, this.calc(i));
        return result;
    }

    public static void toCsv(String path, SortedMap<BigDecimal, BigDecimal> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            for (Map.Entry<BigDecimal, BigDecimal> entry : data.entrySet()) {
                if (entry.getValue() == null) writer.println(entry.getKey() + ", null");
                else
                    writer.println(entry.getKey().setScale(3, RoundingMode.HALF_EVEN) + ", " + entry.getValue().setScale(10, RoundingMode.HALF_EVEN));
            }
        } catch (IOException e) {
            System.err.println("Error while writing to CSV file: " + e.getMessage());
        }
    }
}