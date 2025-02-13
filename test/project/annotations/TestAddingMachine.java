package project.annotations;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestAddingMachine {

	@Test
	public void testSimple() {
		AddingMachine testAddingMachine = new AddingMachine();
		checkComputation(2, 3, testAddingMachine);
	}

	@Test
	public void testFuzzyAdding() {
		//TODO
	}
	
	private void checkComputation(int a, int b, AddingMachine testAddingMachine) {
		long actualResult = testAddingMachine.add(a, b);
		long expectedResult = a;
		expectedResult += b;
		Assert.assertEquals(expectedResult, actualResult);
	}
}
