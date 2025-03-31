package threadingexercise2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestAddingMachine {

	@Test
	public void smokeTest() {
		AddingMachine testMachine = new AddingMachine();
		testMachine.setVal1(2);
		testMachine.setVal2(3);
		Assert.assertEquals(5, testMachine.add());
	}
	
	@Test
	public void testMultiThreaded() {
		AddingMachine testMachine = new AddingMachine();
		
		Callable<Void> add2and3 = () -> {
			synchronized (TestAddingMachine.class)
			testMachine.setVal1(2);
			testMachine.setVal2(3);
			Assert.assertEquals(5, testMachine.add());
			return null;
		};
		
		Callable<Void> add4and5 = () -> {
			synchronized (TestAddingMachine.class)
			testMachine.setVal1(4,5);
		
			Assert.assertEquals(9, testMachine.add());
			return null;
		};
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		List<Future<?>> exceptionChecker = new ArrayList<>();
		for (int i = 0 ; i < 10; i++) {
			exceptionChecker.add(threadPool.submit(add2and3));
			exceptionChecker.add(threadPool.submit(add4and5));
		}
		
		exceptionChecker.forEach(future -> {
			try {
				future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}
