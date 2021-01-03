package gitlet;

import org.junit.Test;
import ucb.junit.textui;

/**
 * The suite of all JUnit tests for the gitlet package.
 *
 * @author Sally Peng
 */
public class UnitTest {

    /**
     * Run the JUnit tests in the loa package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /**
     * A dummy test to avoid complaint.
     */
    @Test
    public void placeholderTest() {
    }

    @Test
    public void alwaysCorrect() {
        assert (true);
    }

    @Test
    public void trivial() {
        assert (1 == 1);
    }

}


