package threading-exercise4(1) (1);

// Pay no attention to the man behind the curtain
public abstract class AbstractExerciseSolver {

	public static interface Exercise {}
	
	public enum Result {
		CORRECT,
		MOSTLY_CORRECT,
		INCORRECT
	}

	protected Result solveExercise(Exercise exercise) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return Result.CORRECT;
	}

	protected Exercise createExercise() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return new Exercise() { };
	}

	public abstract int practiceQuestions(int numQuestionsToPractice) throws Exception;
}
