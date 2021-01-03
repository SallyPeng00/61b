public class LeapYear{
	
	public static void main(String[] args) {
		int year = 2001;
		if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))){
			System.out.println(Integer.toString(year) + " is a leap year");
		} else {
			System.out.println(Integer.toString(year) + " is not a leap year");
		}
	}
}