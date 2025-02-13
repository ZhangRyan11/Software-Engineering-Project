package project.annotations;

import java.util.Random;

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
		long seed = System.currentTimeMillis();
		Random random = new Random(seed);	
		System.out.println("Running with seed:" +seed);
		AddingMachine toTest = new AddingMachine();
		
		for(int i=0;i<100;i++)
		{
			int val1 = random.nextInt();
			int val2 = random.nextInt();
			checkComputation(val1, val2, toTest);
		}
	}
	
	private void checkComputation(int a, int b, AddingMachine testAddingMachine) {
		long actualResult = testAddingMachine.add(a, b);
		long expectedResult = a;
		expectedResult += b;
		System.out.println("adding values"+a+""+b);
		Assert.assertEquals(expectedResult, actualResult);
	}
}
