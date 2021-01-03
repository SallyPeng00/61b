package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** Array Tests
 *  @author Sally Peng
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void catenateTest() {
        int x[] = new int[]{};
        int y[] = new int[]{};
        int z[] = new int[]{};
        assertArrayEquals(z ,Arrays.catenate(x, y));

        int a[] = new int[]{1};
        int b[] = new int[]{2,3,4};
        int c[] = new int[]{1,2,3,4};
        assertArrayEquals(c, Arrays.catenate(a, b));
    }

    @Test
    public void removeTest() {
        int[] a = new int[] {1,2,3,4,5,6,7,8,9,10};
        int[] ans = new int[] {1,2,3,8,9,10};
        assertArrayEquals(ans, Arrays.remove(a,3,4));
    }

    @Test
    public void naturalRunsTest(){
        int[] a = new int[] {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] sln = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}};
        Utils.print(Arrays.naturalRuns(a));
        assertArrayEquals(sln, Arrays.naturalRuns(a));

     }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
