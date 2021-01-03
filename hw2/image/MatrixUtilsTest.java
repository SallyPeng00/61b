package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** Matrix Tests
 *  @author Sally Peng
 */

public class MatrixUtilsTest {
    /** FIXME
     */
    @Test
    public void accumulateVerticalTest() {
        double[][] input = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000,30002, 103046, 1000000},
                {1000000,29515,38273,1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}
        };
        double[][] result = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923,2124919, 2124919, 2124919}
        };
        assertArrayEquals(result, MatrixUtils.accumulateVertical(input));
    }

    @Test
    public void accumulateTest(){
        double[][] input = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000,30002, 103046, 1000000},
                {1000000,29515,38273,1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}
        };
        double[][] vertical = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923,2124919, 2124919, 2124919}
        };

        double[][] simple = new double[][] {
                {1,2,3},
                {4,5,6}
        };

        double[][] horizontal2 = new double[][] {
                {1,3,6},
                {4,6,9}
        };



        assertArrayEquals(vertical, MatrixUtils.accumulate(input, MatrixUtils.Orientation.VERTICAL));
        assertArrayEquals(horizontal2, MatrixUtils.accumulate(simple, MatrixUtils.Orientation.HORIZONTAL));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
