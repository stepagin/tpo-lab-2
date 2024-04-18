import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.stepagin.trigonometry.Sec;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SecTest {

    static Sec sec;
    static final Double accuracy = 0.0001;

    @BeforeAll
    public static void createEntities(){
        sec = new Sec();
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0, -0.5, -0.75, -1.0 })
    public void testValues(double x) {
        double expected = 1 / Math.cos(x);
        double actual = sec.calc(new BigDecimal(x)).doubleValue();
        assertEquals(expected, actual, accuracy);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-Math.PI / 2, Math.PI / 2, 3 * Math.PI / 2, -5 * Math.PI / 2 })
    public void testNulls(double x) {
        assertNull(sec.calc(BigDecimal.valueOf(x)));
    }
}
