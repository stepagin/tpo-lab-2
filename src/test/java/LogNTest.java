import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.stepagin.logarithms.LogN;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LogNTest {
    static final Double accuracy = 0.001;

    @ParameterizedTest(name = "testPositiveSmall")
    @ValueSource(doubles = {0.1, 10, 100, 1000})
    public void testPositiveSmall(double x) {
        LogN logN = new LogN(BigDecimal.valueOf(10));
        assertEquals(Math.log10(x), logN.calc(BigDecimal.valueOf(x)).doubleValue(), accuracy);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, -2, 0, 1})
    public void testThrows(double base) {
        assertThrows(IllegalArgumentException.class, () -> {
            LogN logN = new LogN(BigDecimal.valueOf(base));
        });
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, -2, -500, -0.0001, 0})
    public void testNulls(double x) {
        LogN log2 = new LogN(BigDecimal.valueOf(2));
        LogN log5 = new LogN(BigDecimal.valueOf(5));
        LogN log05 = new LogN(BigDecimal.valueOf(0.5));

        assertNull(log2.calc(BigDecimal.valueOf(x)));
        assertNull(log5.calc(BigDecimal.valueOf(x)));
        assertNull(log05.calc(BigDecimal.valueOf(x)));
    }
}