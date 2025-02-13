package exercise3;

import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class TestCombinatorics {

	@Test
	public void smokeTest() {
		Combinatorics testCombinatorics = new Combinatorics();
		
		// P(5,3) = 60
		Assert.assertEquals(60, testCombinatorics.permutation(5, 3));
		
		// C(5,3) = 10
		Assert.assertEquals(10, testCombinatorics.combination(5,3));
	}
}
