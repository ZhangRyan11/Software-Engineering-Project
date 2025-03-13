package threadingexercise2;

public class AddingMachine {

	private int val1;
	private int val2;
	
	public void setVal1(int val1, int val2) {
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public int add() {
		return val1 + val2;
	}
}
