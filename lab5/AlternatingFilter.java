import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.Filter;


import javax.swing.text.html.HTMLDocument;

/** A kind of Filter that lets through every other VALUE element of
 *  its input sequence, starting with the first.
 *  @author Sally Peng
 */
class AlternatingFilter<Value> extends Filter<Value> {



    /** A filter of values from INPUT that lets through every other
     *  value. */
    AlternatingFilter(Iterator<Value> input) {
        super(input);
        keepOrNot = false;
    }

    @Override
    protected boolean keep() {
        keepOrNot = !keepOrNot;
        return keepOrNot;
    }

    private boolean keepOrNot;

}