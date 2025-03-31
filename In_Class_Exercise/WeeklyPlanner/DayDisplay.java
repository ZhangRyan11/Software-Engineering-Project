
public class DayDisplay {

	private static final int MONTHS_IN_YEAR = 12;
	
	private int dayOfMonth;
	private int monthOfYear;
	private int year;
	
	public DayDisplay(int month, int day, int year) {
		this.dayOfMonth = day;
		this.monthOfYear = month;
		this.year = year;
	}
	
	public DayDisplay nextDay() {
		dayOfMonth++;
		if (dayOfMonth > daysInMonth()) {
			dayOfMonth = 1;
			monthOfYear++;
		}
		if (monthOfYear > MONTHS_IN_YEAR) {
			monthOfYear = 1;
			year++;
		}
		return this;
	}
	
	private int daysInMonth() {
		switch (monthOfYear) {
		// 30 days hath September, April, June, and November
		case 9:
		case 4:
		case 6:
		case 11:
			return 30;
		case 2:
			return (year % 4 == 0 && year % 100 != 0) ? 29 : 28;
		default:
			return 31;
		}
	}

	@Override
	public String toString() {
		return monthOfYear + "/" + dayOfMonth + "/" + year;
	}
}
