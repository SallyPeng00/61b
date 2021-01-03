package enigma;
import java.util.ArrayList;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Sally Peng
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String rawCycles = cycles;
        rawCycles = rawCycles.replace(")", " ");
        rawCycles = rawCycles.replace("(", " ");
        String[] temp = rawCycles.split(" ");
        ArrayList<String> c = new ArrayList<>();
        for (String cycle : temp) {
            if (!cycle.equals("")) {
                c.add(cycle);
            }
        }
        _cycles = new String[c.size()];
        for (int i = 0; i < c.size(); i++) {
            _cycles[i] = c.get(i);
        }
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        String[] newCycles = new String[_cycles.length + 1];
        System.arraycopy(_cycles, 0, newCycles, 0, _cycles.length);
        newCycles[_cycles.length + 1] = cycle;
        _cycles = newCycles;
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        int index = wrap(p);
        char original = _alphabet.toChar(index);
        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length(); j++) {
                if (_cycles[i].charAt(j) == original) {
                    return _alphabet.toInt(_cycles[i].charAt((j + 1)
                            % _cycles[i].length()));
                }
            }
        }
        return index;
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(i) == c % _alphabet.size()) {
                return i;
            }
        }
        return c;
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        int pIndex = _alphabet.toInt(p);
        return _alphabet.toChar(permute(pIndex));
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(_alphabet.toChar(i)) == c) {
                return _alphabet.toChar(i);
            }
        }
        return c;
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(_alphabet.get(i)) == _alphabet.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;

    /**
     * String array to keep track of each cycle.
     */
    private String[] _cycles;

}
