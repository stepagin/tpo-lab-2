import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import ru.stepagin.EquationSystem;
import ru.stepagin.Function;
import ru.stepagin.logarithms.Ln;
import ru.stepagin.logarithms.LogN;
import ru.stepagin.trigonometry.Sec;
import ru.stepagin.trigonometry.Sin;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.mockito.Mockito.when;

public class EquationSystemTest {

    static final String inputPath = "./EquationSystemInput.csv";
    static Sin sinMock;
    static Ln lnMock;
    static LogN log3Mock;
    static LogN log5Mock;

    static Sec secMock;

    static <T extends Function> T setSpy(T mockClass, java.util.function.Function<Double, BigDecimal> f, int from, int to) {
        T functionSpy = Mockito.spy(mockClass);
        for (int i = from; i <= to; i++) {
            BigDecimal res = f.apply((double) i / 10);
            if (res == null) continue;
            when(functionSpy.calc(BigDecimal.valueOf(i / 10))).thenReturn(res);
        }

        return functionSpy;
    }


    @BeforeAll
    static void init() {
        Sin sin = new Sin();
        sinMock = setSpy(sin, (x) -> BigDecimal.valueOf(Math.sin(x)), -10, 50);
        Ln ln = new Ln();
        lnMock = setSpy(ln, (x) -> BigDecimal.valueOf(Math.log(x)), 1, 50);
        LogN log3 = new LogN(3);
        log3Mock = setSpy(log3, (x) -> BigDecimal.valueOf(Math.log(x) / Math.log(3)), 1, 50);
        LogN log5 = new LogN(5);
        log5Mock = setSpy(log5, (x) -> BigDecimal.valueOf(Math.log(x) / Math.log(5)), 1, 50);
        Sec sec = new Sec();
        secMock = setSpy(sec, (x) -> BigDecimal.valueOf(1 / Math.cos(x)), -10, 50);
    }


    @ParameterizedTest(name = "no mock test")
    @CsvFileSource(resources = inputPath)
    void testNoMock(double x, double y) {
        BigDecimal X = new BigDecimal(x);
        EquationSystem equationSystem = new EquationSystem();
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = new BigDecimal(y);
            if (Math.abs(y) < 1)
                Assertions.assertEquals(Y.setScale(2, RoundingMode.HALF_EVEN), equationSystem.calc(X).setScale(2, RoundingMode.HALF_EVEN));
            else
                Assertions.assertEquals(Y.setScale(3, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(3, RoundingMode.HALF_UP));
        }

    }

    @ParameterizedTest(name = "all mock test")
    @CsvFileSource(resources = inputPath)
    void testOnlyMock(double x, double y) {
        BigDecimal X = new BigDecimal(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(lnMock);
        equationSystem.setLog5(log5Mock);
        equationSystem.setLog3(log3Mock);
        equationSystem.setSin(sinMock);
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = new BigDecimal(y);
            Assertions.assertEquals(Y.setScale(3, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(3, RoundingMode.HALF_UP));
        }

    }

    @ParameterizedTest(name = "Ln mock test")
    @CsvFileSource(resources = inputPath)
    void testLnMock(double x, double y) {
        BigDecimal X = new BigDecimal(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(lnMock);
        equationSystem.setLog5(new LogN(BigDecimal.valueOf(5), lnMock));
        equationSystem.setLog3(new LogN(BigDecimal.valueOf(3), lnMock));
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = new BigDecimal(y);
            Assertions.assertEquals(Y.setScale(3, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(3, RoundingMode.HALF_UP));
        }
    }

    @ParameterizedTest(name = "Sec test")
    @CsvFileSource(resources = inputPath)
    void testSec(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x).setScale(10, RoundingMode.HALF_EVEN);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(lnMock);
        equationSystem.setLog5(log5Mock);
        equationSystem.setLog3(log3Mock);
        equationSystem.setSin(sinMock);
        equationSystem.setSec(new Sec());
        if (Double.isNaN(y))
            Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            Assertions.assertEquals(
                    Y.setScale(3, RoundingMode.HALF_UP),
                    equationSystem.calc(X).setScale(3, RoundingMode.HALF_UP));
        }
    }

    @ParameterizedTest(name = "Sin test")
    @CsvFileSource(resources = inputPath)
    void testSin(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(lnMock);
        equationSystem.setLog5(log5Mock);
        equationSystem.setLog3(log3Mock);
        equationSystem.setSin(new Sin());
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            Assertions.assertEquals(Y.setScale(3, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(3, RoundingMode.HALF_UP));
        }
    }

    @ParameterizedTest(name = "Ln test")
    @CsvFileSource(resources = inputPath)
    void testLn(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(new Ln());
        equationSystem.setLog5(log5Mock);
        equationSystem.setLog3(log3Mock);
        equationSystem.setSin(sinMock);
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            if (Math.abs(y) < 1)
                Assertions.assertEquals(Y.setScale(2, RoundingMode.HALF_EVEN), equationSystem.calc(X).setScale(2, RoundingMode.HALF_EVEN));
            else
                Assertions.assertEquals(Y.setScale(1, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(1, RoundingMode.HALF_UP));
        }


    }

    @ParameterizedTest(name = "Log3 test")
    @CsvFileSource(resources = inputPath)
    void testLog3(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(lnMock);
        equationSystem.setLog5(log5Mock);
        equationSystem.setLog3(new LogN(BigDecimal.valueOf(3)));
        equationSystem.setSin(sinMock);
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            if (Math.abs(y) < 1)
                Assertions.assertEquals(Y.setScale(2, RoundingMode.HALF_EVEN), equationSystem.calc(X).setScale(2, RoundingMode.HALF_EVEN));
            else
                Assertions.assertEquals(Y.setScale(1, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(1, RoundingMode.HALF_UP));
        }
    }

    @ParameterizedTest(name = "Log5 test")
    @CsvFileSource(resources = inputPath)
    void testLog5(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLn(lnMock);
        equationSystem.setLog5(new LogN(5));
        equationSystem.setLog3(log3Mock);
        equationSystem.setSin(sinMock);
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            if (Math.abs(y) < 1)
                Assertions.assertEquals(Y.setScale(2, RoundingMode.HALF_EVEN), equationSystem.calc(X).setScale(2, RoundingMode.HALF_EVEN));
            else
                Assertions.assertEquals(Y.setScale(1, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(1, RoundingMode.HALF_UP));
        }
    }

    @ParameterizedTest(name = "only logN test")
    @CsvFileSource(resources = inputPath)
    void testLogN(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setSin(sinMock);
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            if (Math.abs(y) < 1)
                Assertions.assertEquals(Y.setScale(2, RoundingMode.HALF_EVEN), equationSystem.calc(X).setScale(2, RoundingMode.HALF_EVEN));
            else
                Assertions.assertEquals(Y.setScale(1, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(1, RoundingMode.HALF_UP));
        }

    }

    @ParameterizedTest(name = "sin + ln test")
    @CsvFileSource(resources = inputPath)
    void testSinLn(double x, double y) {
        BigDecimal X = BigDecimal.valueOf(x);
        EquationSystem equationSystem = new EquationSystem();
        equationSystem.setLog5(log5Mock);
        equationSystem.setLog3(log3Mock);
        equationSystem.setSec(secMock);
        if (Double.isNaN(y)) Assertions.assertNull(equationSystem.calc(X));
        else {
            BigDecimal Y = BigDecimal.valueOf(y);
            if (Math.abs(y) < 1)
                Assertions.assertEquals(Y.setScale(2, RoundingMode.HALF_EVEN), equationSystem.calc(X).setScale(2, RoundingMode.HALF_EVEN));
            else
                Assertions.assertEquals(Y.setScale(1, RoundingMode.HALF_UP), equationSystem.calc(X).setScale(1, RoundingMode.HALF_UP));
        }

    }

}