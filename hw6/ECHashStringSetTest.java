import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void test() {
        ECHashStringSet e = new ECHashStringSet();
        assertFalse(e.contains("blah"));
    }
}