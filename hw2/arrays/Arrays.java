package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Sally Peng
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        if (A == null) {return B;}
        int [] result = new int[A.length+B.length];
        for (int i = 0; i < A.length; i++) {
            result[i] = A[i];
        }
        for (int j= 0; j< B.length; j++) {
            result[j + A.length] = B[j];
        }

        return result;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        if (A == null){
            return null;
        }
        int[] front = new int[start];
        int[] end = new int[A.length-start-len];
        for (int i = 0; i < start; i++) {
            front[i] = A[i];
        }
        for (int j = start + len; j < A.length; j++) {
            end[j-start-len] = A[j];
        }
        return catenate(front, end);
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        if (A == null){
            return null;
        }
        int subarrays = 1;
        //determine the number of sub-arrays
        for (int i = 0;i < A.length -1; i++){
            if (A[i]>=A[i+1]) {
                subarrays += 1;
            }
        }
        int[][] result = new int[subarrays][];
        int tracker = 0;
        for(int i = 0; i < subarrays; i ++) {
            int index = tracker;
            int len = 1;
            while (tracker < A.length-1 && A[tracker] < A[tracker + 1]) {
                len += 1;
                tracker += 1;
            }
            tracker += 1;
            result[i] = Utils.subarray(A,index,len);
        }




//        int tracker = 0;
//        for(int i = 0; i < subarrays; i++) {
//            int start = tracker;
//            int len = 1;
//            while (tracker < A.length-1) {
//                while (A[tracker] < A[tracker + 1]) {
//                    len += 1;
//                }
//                tracker += 1;
//            }
//            result[i] = Utils.subarray(A,start,len);
//        }

        return result;
    }

}
