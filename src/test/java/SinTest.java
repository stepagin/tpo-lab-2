import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.stepagin.trigonometry.Sin;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SinTest {

    static Sin sin;
    static final Double accuracy = 0.0000001;

    @BeforeAll
    public static void createEntities(){
        sin = new Sin();
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, -1, -0.5, -0.75, -1.0 })
    public void testValues(double x) {
        assertEquals(Math.sin(x), sin.calc(BigDecimal.valueOf(x)).doubleValue(), accuracy);
    }
}