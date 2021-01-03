public class ThreeSum_dist{
	public static boolean threeSumDistinct(int[] a){
		for (int i = 0; i < a.length - 2; i++){
			for (int j = i + 1; j < a.length - 1; j++){
				for (int k = j + 1; k < a.length; k++){
					if (a[i] + a[j] +a[k] == 0){
						return true;
					}
					
				}
			}
		}
		return false;
	}

	public static void main(String[] args){
		boolean a = threeSumDistinct(new int[]{-6, 2, 4});
		boolean b = threeSumDistinct(new int[]{8, 2, -1, -1, 15});
		boolean c = threeSumDistinct(new int[]{8, 2, -1, 15});
		System.out.println(a + "\n" + b + "\n" +c);
	}
}