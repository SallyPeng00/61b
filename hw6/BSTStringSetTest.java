import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {

    @Test
    public void testPut() {
        BSTStringSet t = new BSTStringSet();
        t.put("hello");
        assertTrue(t.contains("hello"));
    }

    @Test
    public void testAsList() {
        BSTStringSet t = new BSTStringSet();
        t.put("hello");
        List<String> ans = new ArrayList<>();
        ans.add("hello");
        assertEquals(ans, t.asList());

        t.put("iello");
        assertTrue(t.contains("iello"));
        ans.add("iello");
        assertEquals(ans, t.asList());

        t.put("gello");
        assertTrue(t.contains("gello"));
        ans.add(0,"gello");
        assertEquals(ans, t.asList());

        t.put("rando");
        ans.add("rando");
        assertEquals(ans, t.asList());


        t.put("jello");
        ans.add(3, "jello");
        assertEquals(ans, t.asList());

        t.put("airpods");
        ans.add(0, "airpods");
        assertEquals(ans, t.asList());

        t.put("aaa");
        ans.add(0,"aaa");
        assertEquals(ans, t.asList());

        t.put("aab");
        ans.add(1, "aab");
        assertEquals(ans, t.asList());
    }

}
