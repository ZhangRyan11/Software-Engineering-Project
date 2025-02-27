
public class WeeklyPlanner {

	private static final int DAYS_IN_WEEK = 7;

	public static void main(String[] args) {
		DayDisplay currentDay = new DayDisplay(9, 25, 2024);
		for (int i = 0; i < DAYS_IN_WEEK; i++) {
			printDayInfo(currentDay);
			//currentDay.nextDay();
			
		}
	}

	private static void printDayInfo(DayDisplay dayToPrint) {
		System.out.println("Info for the day " + dayToPrint + " (the next day will be " + updatedDay + ")");
		return updatedDay;
	}
	
	
	
}
