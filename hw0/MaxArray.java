public class MaxArray {
	public static int max(int[] a) {
		int max = a[0];
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	public static void main(String[] args) {
		int[] example = new int[]{3, 5, 4, 1};
		System.out.println(max(example));
	}
}