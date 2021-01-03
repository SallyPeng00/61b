public class ThreeSum {
	public static boolean threeSum(int[] a){
		for (int i = 0; i < a.length; i++){
			for (int j = i; j < a.length; j++){
				for (int k = j; k < a.length; k++){
					if (a[i] + a[j] +a[k] == 0){
						return true;
					}
					
				}
			}
		}
		return false;
	}

	public static void main(String[] args){
		boolean a = threeSum(new int[]{-6, 2, 4});
		boolean b = threeSum(new int[]{8, 2, -1, -1, 15});
		boolean c = threeSum(new int[]{5, 1, 0, 3, 6});
		System.out.println(a + "\n" + b + "\n" +c);
	}
}