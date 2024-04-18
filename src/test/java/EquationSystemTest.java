import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
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

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class EquationSystemTest {

    static final String inputPath = "./EquationSystemInput.csv";
    static Sin sinMock;
    static Ln lnMock;
    static LogN log3Mock;
    static LogN log5Mock;

    static Sec secMock;

    static final Double accuracy = 0.0001;

    static <T extends Function> T setMock(String path, Class<T> mockClass) {
        T mockInstance = Mockito.mock(mockClass);
        try {
            Reader reader = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                Double input = (Double.parseDouble(record.get(0)));
                BigDecimal output;
                if (record.get(1).equals("NaN")) output = BigDecimal.valueOf(Double.NaN);
                else {
                    output = BigDecimal.valueOf(Double.parseDouble(record.get(1)));
                }
                Mockito.when(mockInstance.calc(new BigDecimal(input))).thenReturn((BigDecimal) output);
            }

        } catch (IOException e) {
            throw new RuntimeException("Не найден файл или не удалось его прочитать: " + e.getMessage());
        }
        return mockInstance;
    }


    @BeforeAll
    static void init() {
        sinMock = setMock( "./csv/SinInput.csv", Sin.class);
        lnMock = setMock(  "./csv/LnInput.csv", Ln.class);
        log3Mock = setMock("./csv/Log3Input.csv", LogN.class);
        log5Mock = setMock("./csv/Log5Input.csv", LogN.class);
        secMock = setMock( "./csv/SecInput.csv", Sec.class);
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