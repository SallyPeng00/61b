package enigma;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 * @author Sally Peng
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _origNotches = notches;
    }


    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(this.setting() + 1);
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            char notchChar = _notches.charAt(i);
            if (setting() % alphabet().size() == alphabet().toInt(notchChar)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setRing(int ringSetting) {
        super.setRing(ringSetting);
        _notches = _origNotches;
        StringBuilder newNotches = new StringBuilder();
        for (int i = 0; i < _notches.length(); i++) {
            int notch = alphabet().toInt(_notches.charAt(i));
            notch -= ringSetting;
            newNotches.append(alphabet().toChar(wrap(notch)));
        }
        _notches = newNotches.toString();
    }

    /**
     * notches.
     */
    private String _notches;

    /** keeping track of the original notches. */
    private String _origNotches;

}
