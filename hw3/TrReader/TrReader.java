import java.io.IOException;
import java.io.Reader;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Sally Peng
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

    private Reader _reader;
    private String _from;
    private String _to;
    public TrReader(Reader str, String from, String to) {
        _reader = str;
        _from = from;
        _to = to;
    }

    public char translate(char c) {
        for (int i = 0; i < _from.length(); i ++) {
            if (c == _from.charAt(i)) {
                return _to.charAt(i);
            }
        }
        return c;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        int k = _reader.read(cbuf, off, len);
        for (int i = off; i < k; i ++) {
            cbuf[i] = translate(cbuf[i]);
        }
        return k;
    }

    public void close() throws IOException{
        _reader.close();
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */

}
