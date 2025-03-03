package exercise1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/* Don't worry about this class for right now */
public class AbstractGame {
	/* Don't worry about the code below this point for now */
	private AtomicInteger total = new AtomicInteger();
	public boolean gameIsOver() {
		return total.get() >= 21;
	}

	protected void playCard() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		int card = new Random().nextInt(13) + 1;
		for (int i = 0; i < card; i++) {
			total.incrementAndGet();
		}
	}
}
