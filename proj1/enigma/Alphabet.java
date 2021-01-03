package enigma;

/**
 * An alphabet of encodable characters.  Provides a mapping from characters
 * to and from indices into the alphabet.
 *
 * @author Sally Peng
 */
class Alphabet {

    /**
     * array of chars to store the alphabet.
     */
    private Character[] _chars;

    /**
     * A new alphabet containing CHARS.  Character number #k has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        this._chars = new Character[chars.length()];
        for (int i = 0; i < this.size(); i++) {
            _chars[i] = chars.charAt(i);
            for (int j = 0; j < i; j++) {
                if (_chars[i] == _chars[j]) {
                    throw new EnigmaException("Duplicated character");
                }
            }
        }
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return this._chars.length;
    }

    /**
     * Returns true if preprocess(CH) is in this alphabet.
     */
    boolean contains(char ch) {
        for (int i = 0; i < this.size(); i++) {
            if (_chars[i] == ch) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Index out of bounds of Alphabet");
        }
        return _chars[index];
    }

    /**
     * Returns the index of character preprocess(CH), which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("Char is not in the Alphabet");
        }
        for (int index = 0; index < this.size(); index++) {
            if (_chars[index] == ch) {
                return index;
            }
        }
        return -1;
    }

    /**
     *  Returns alphabet letter at position i.
     * @param i index
     * @return alphabet letter at i
     */
    char get(int i) {
        return _chars[i];
    }
}
