package enigma;

/** Class that represents a rotor that has no ratchet and does not advance.
 *
 * @author Sally Peng
 */
class FixedRotor extends Rotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM.
     */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    /**
     * set position.
     * @param posn position of the rotor.
     */
    void set(int posn) {
        if (posn != 0) {
            throw new EnigmaException("Fixed Rotor cannot rotate!");
        }
    }
}
