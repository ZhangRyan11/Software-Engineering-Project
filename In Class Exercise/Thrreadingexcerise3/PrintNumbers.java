package Thrreadingexercise3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PrintNumbers {

	public List<Integer> printNumbers(int n) {
		// TODO: add type information to this list to fix the warning
		List result = new ArrayList<>();
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		List<Future<?>> exceptionChecker = new ArrayList<>();
		//Semaphore s = new Semaphore(10, true);
		//CountDownLatch latch = new CountDownLatch(______);
		int numPermits = 0;
		Semaphore prevSemaphore =null;
		Semaphore nextSemaphore = null;
		for (int i = 0 ; i < n; i++) {
			//make this thread acquire some semaphore, and have it return once the (i+1)st thread runs
			if(i==n-1)
			nextSemaphore s = new Semaphore(numPermit, true);
			Semaphore finalNext = nextSemaphore;
			Semaphore finalPrev = prevSemaphore;
			int final = i;
			Callable<Void> printingThread = getPrinter(i, result);
			{
				//call acquire
				nextSemaphore.acquire();
				try {
					future.get(1, TimeUnit.MINUTES)
				}
				doSlowComputation(valToPrint);
				result.add(valToPrint);
				}
				finally {
				prevSemaphore.release();
				if(finalPrev != null) {
					finalPrev.release();
				}
				return null;
			}
			exceptionChecker.add(threadPool.submit(getPrinter(i, result)));
		}
		prevSemaphore = nextSemaphore;
		
				
		exceptionChecker.forEach(future -> {
			try {
				future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		//TODO: this list should be sorted highest to lowest
		return result;
	}

	private Callable<Void> getPrinter(int valToPrint, List result) {
		return () -> { 
			doSlowComputation(valToPrint);
			result.add(valToPrint);
			return null;
		};
	}

	private void doSlowComputation(int valToPrint) {
		// no, this isn't actually slow
		int result = 1;
		for (int i = 1; i <= valToPrint; i++) {
			result *= i;
		}
		System.out.println(result);
	}
}
