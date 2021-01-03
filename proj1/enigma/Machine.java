package enigma;

import java.util.Collection;
import java.util.HashSet;


/**
 * Class that represents a complete enigma machine.
 *
 * @author Sally Peng
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotors = new Rotor[_numRotors];
        _plugboard = new Permutation("", new Alphabet());
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        if (_rotors.length != rotors.length) {
            throw new EnigmaException("wrong rotor length");
        }

        for (int r = 0; r < rotors.length; r++) {
            for (Rotor rotor : _allRotors) {
                if (rotor.name().equals(rotors[r])) {
                    _rotors[r] = rotor;
                }
            }
        }

        for (int i = 0; i < _rotors.length; i++) {
            if (_rotors[i] == null) {
                System.out.println(i);
                throw new EnigmaException("misnamed rotor");
            }
        }

        int moving = 0;
        for (int i = 0; i < _rotors.length; i++) {
            if (_rotors[i].rotates()) {
                moving += 1;
            }
            for (int j = 0; j < i; j++) {
                if (_rotors[j].name() == _rotors[i].name()) {
                    throw new EnigmaException("duplicated rotors");
                }
            }
        }
        if (moving != numPawls()) {
            throw new EnigmaException("wrong number of moving rotors");
        }

        if (!_rotors[0].reflecting()) {
            throw new EnigmaException("0th rotor must be a reflector");
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        if (setting.length() != _rotors.length - 1
                && setting.length() != 2 * _rotors.length - 1) {
            throw new EnigmaException("Initial "
                    + "setting wrong length in setRotors");
        }
        for (int i = 0; i < _rotors.length - 1; i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Initial setting "
                        + "not found in alphabet");
            }
            _rotors[i + 1].set(setting.charAt(i));
        }

        if (setting.length() > _rotors.length - 1) {
            String[] ringSet = setting.split(" ");
            String ringSetting = ringSet[1];
            for (int i = 0; i < ringSetting.length(); i++) {
                _rotors[i + 1].setRing(_alphabet.toInt(ringSetting.charAt(i)));
            }
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        HashSet<Rotor> advRotors = new HashSet<>();
        advRotors.add(_rotors[_rotors.length - 1]);
        for (int r1 = 1; r1 < _rotors.length - 1; r1++) {
            if (_rotors[r1 + 1].atNotch() && _rotors[r1].rotates()) {
                advRotors.add(_rotors[r1]);
                advRotors.add(_rotors[r1 + 1]);
            }
        }
        for (Rotor rotor : advRotors) {
            rotor.advance();
        }

        int output = _plugboard.permute(c);
        for (int m = _rotors.length - 1; m >= 0; m--) {
            output = _rotors[m].convertForward(output);
        }
        for (int n = 1; n < _rotors.length; n++) {
            output = _rotors[n].convertBackward(output);
        }
        output = _plugboard.permute(output);
        return output;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        msg = msg.replace(" ", "");
        String output = "";
        for (int a = 0; a < msg.length(); a++) {
            char ch = msg.charAt(a);
            if (_alphabet.contains(ch)) {
                int c = _alphabet.toInt(ch) % _alphabet.size();
                output += Character.toString(_alphabet.toChar(convert(c)));
            } else {
                throw new EnigmaException("char must be in alphabet!");
            }
        }
        return output;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * Number of rotors.
     */
    private int _numRotors;

    /**
     * Number of pawls.
     */
    private int _pawls;

    /**
     * Collection of rotors, all possible rotors.
     */
    private Collection<Rotor> _allRotors;

    /**
     * Plugboard setting.
     */
    private Permutation _plugboard;

    /**
     * Array of rotors inserted.
     */
    private Rotor[] _rotors;
}
