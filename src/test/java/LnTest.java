import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.stepagin.logarithms.Ln;
import ru.stepagin.logarithms.LogN;
import ru.stepagin.trigonometry.Cos;
import ru.stepagin.trigonometry.Sec;
import ru.stepagin.trigonometry.Sin;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LnTest {
    static Ln ln;
    static Double accuracy = 0.0001;

    @BeforeAll
    public static void createEntities() {
        ln = new Ln();
    }
    @ParameterizedTest
    @ValueSource(doubles = {0.1, 1 / Math.E, 0.5, 1, Math.E, Math.E * Math.E})
    public void testValues(double x) {
        assertEquals(Math.log(x), ln.calc(BigDecimal.valueOf(x)).doubleValue(), accuracy);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 0, -0.2})
    public void testNulls(double x) {
        assertNull(ln.calc(BigDecimal.valueOf(x)));
    }
}
