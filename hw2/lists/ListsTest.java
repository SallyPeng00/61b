package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** Test for Lists
 *
 *  @author Sally Peng
 */

public class ListsTest {
    /** FIXME
     */
    @Test
    public void ListsTests() {
        IntListList ans = Lists.naturalRuns(IntList.list(1, 3, 7, 5, 4, 6, 9, 10,10,11));
        assertEquals(new IntListList(IntList.list(1,3,7), new IntListList(IntList.list(5), new IntListList(IntList.list(4,6,9,10), new IntListList(IntList.list(10,11),null)))), ans);
        assertEquals(Lists.naturalRuns(null),null);
        assertEquals(new IntListList(IntList.list(1,2), new IntListList(IntList.list(1), new IntListList(IntList.list(0), null))), Lists.naturalRuns(IntList.list(1, 2, 1, 0)));
    }

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {

    }
}
