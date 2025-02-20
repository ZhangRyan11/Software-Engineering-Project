package exercise2;

public class HoneyBadger implements Animal {

	@Override
	public boolean tryToPet() {
		throw new RuntimeException("That was a bad idea.");
	}

	@Override
	public boolean isFriendly() {
		return false;
	}

}
