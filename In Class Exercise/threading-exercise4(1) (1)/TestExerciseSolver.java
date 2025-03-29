package threading-exercise4(1) (1);

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestExerciseSolver {

	@Test
	public void smokeTest() throws Exception {
		AbstractExerciseSolver solver = new ExerciseSolver();
		solver.practiceQuestions(1);
	}
	
	@Test
	public void testFullyPipelined() throws Exception {
		AbstractExerciseSolver solver = new ExerciseSolver();
		long start = System.currentTimeMillis();
		Assert.assertEquals(4, solver.practiceQuestions(4));
		long end = System.currentTimeMillis();
		
		Assert.assertTrue("Pipeline isn't fully pipelined!", (end - start) < 7000); 
	}
}
