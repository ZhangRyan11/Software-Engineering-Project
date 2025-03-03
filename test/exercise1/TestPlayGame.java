package exercise1;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestPlayGame {

	@Test
	public void smokeTest() {
		PlayGame game = new PlayGame();
		game.playGame();
		Assert.assertEquals(true, game.gameIsOver());
	}
}
