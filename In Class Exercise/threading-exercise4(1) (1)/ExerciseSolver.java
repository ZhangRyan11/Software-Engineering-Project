package exercise4;

public class ExerciseSolver extends AbstractExerciseSolver {

	public int practiceQuestions(int numPracticeQuestions) throws Exception {
		int numSolved = 0;
		
		// TODO: Create a pipeline with 2 stages that can run at once: 
		// creating the exercise and solving the exercise
		for (int i = 0; i < numPracticeQuestions; i++) {
			Exercise exercise = createExercise();
			Result result = solveExercise(exercise);
			if (result == Result.CORRECT || result == Result.MOSTLY_CORRECT) {
				numSolved++;
			}
		}
		return numSolved;
	}
}
