package enigma;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.HashMap;

import static enigma.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**The suite of all JUnit tests for the Permutation class.
 *
 * @author Sally Peng
 */
public class MovingRotorTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /**
     * Check that rotor has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    /**
     * Set the rotor to the one with given NAME and permutation as
     * specified by the NAME entry in ROTORS, with given NOTCHES.
     */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void movingRotor() {
        Permutation p1 = new Permutation("(AELTPHQXRU) (BKNW) "
                + "(CMOY) (DFG) (IV) (JZ) (S)", new Alphabet());
        MovingRotor r1 = new MovingRotor("Rotor I", p1, "Q");
        r1.advance();
        assertEquals(1, r1.setting());
        assertEquals(9, r1.convertForward(0));
        r1.set(14);
        assertEquals(14, r1.setting());
        assertEquals(20, r1.convertForward(7));
        assertEquals(1, r1.convertBackward(19));
    }

    @Test
    public void fixedRotor() {
        Permutation b = new Permutation("(ALBEVFCYODJWUGNMQTZS"
                + "KPR) (HIX)", new Alphabet());
        FixedRotor beta = new FixedRotor("Rotor Beta", b);
        assertFalse(beta.rotates());
        beta.advance();
        assertEquals(0, beta.setting());
        try {
            beta.set(3);
        } catch (EnigmaException e) {
            System.out.println("cannot set rotor");
        }
    }
}
