import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.stepagin.trigonometry.Cos;
import ru.stepagin.trigonometry.Sin;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CosTest {

    static Cos cos;
    static final Double accuracy = 0.001;

    @BeforeAll
    public static void createEntities(){
        cos = new Cos();
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 0.1, -0.5, -0.75, -1.0 })
    public void testValues(double x) {
        assertEquals(Math.cos(x), cos.calc(BigDecimal.valueOf(x)).doubleValue(), accuracy);
    }
}
