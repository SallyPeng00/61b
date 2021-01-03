import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets all the VALUE elements of its input sequence
 *  through.
 *  @author Sally Peng
 */
class TrivialFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that simply delivers all of them. */
    TrivialFilter(Iterator<Value> input) {
        super(input);
    }

    @Override
    protected boolean keep() {
        return true;
    }
}
