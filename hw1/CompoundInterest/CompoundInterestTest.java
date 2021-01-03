import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(2, CompoundInterest.numYears(2021));
        assertEquals(10, CompoundInterest.numYears(2029));
    }

    @Test
    public void testFutureValue() {
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2021),0.01);
        assertEquals(8.115, CompoundInterest.futureValue(7, 3, 2024),0.01);
    }

    @Test
    public void testFutureValueReal() {
        assertEquals(11.8, CompoundInterest.futureValueReal(10, 12, 2021, 3), 0.01);
        assertEquals(7.34, CompoundInterest.futureValueReal(7, 3, 2024, 2), 0.01);
    }

    @Test
    public void testTotalSavings() {
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2021, 10), 0.01);
    }

    @Test
    public void testTotalSavingsReal() {
        assertEquals(15572, CompoundInterest.totalSavingsReal(5000, 2021, 10, 3), 1);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
