package threading-exercise4(1) (1);

public class ExerciseSolver extends AbstractExerciseSolver {

	public int practiceQuestions(int numPracticeQuestions) throws Exception {
		AtomicInteger numSolved = 0;
		BlockingQueue<Exercise> handoffQueue = new ArrayBlockingQueue<>(10);
		AtomicBoolean allExercisesQueued = new AtomicBoolean(false);
		
		Callable<Void> exercisePhase = () -> {
			try {
			for (int i = 0; i < numPracticeQuestions; i++) {
				Exercise exercise = createExercise();
				handoffQueue.offer(exercise);
			}
		} finally {
			allExercisesQueued.set(true);
		}
		};
		Callable<Void> solvePhase = () -> {
			while (!allExercisesQueued.get()|| !handoffQueue.isEmpty()) {
				Exercise exercise = handoffQueue.poll(1, TimeUnit.SECONDS);
				
			}
			
			Result result = solveExercise(exercise);
			if (result == Result.CORRECT || result == Result.MOSTLY_CORRECT) {
				numSolved.incrementAndGet();
			}
			return null;
		}
		
		// TODO: Create a pipeline with 2 stages that can run at once:
		//private static final int MAX_COUNTER_SPACE=10;
		public Collection<Exercise>
		// creating the exercise and solving the exercise
		
		return numSolved;
	}
}
