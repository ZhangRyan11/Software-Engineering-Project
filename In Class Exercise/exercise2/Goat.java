package exercise2;

public class Goat implements Animal {

	@Override
	public boolean tryToPet() {
		System.out.println("Mehhhh!");
		return true;
	}

	@Override
	public boolean isFriendly() {
		return true;
	}
}
