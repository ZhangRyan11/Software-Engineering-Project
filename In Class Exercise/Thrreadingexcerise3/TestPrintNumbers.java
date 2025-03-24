package Thrreadingexcerise3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestPrintNumbers {

	@Test
	public void testSorting() {
		PrintNumbers testNumbers = new PrintNumbers();
		List<Integer> shouldBeSorted = testNumbers.printNumbers(10);
		List<Integer> actuallySorted = new ArrayList<>(shouldBeSorted);
		
		// Collections is a useful class. It can do an in-place sorting of lists,
		// which for integers by default sorts from lowest to highest.
		// Here, by specifying a Comparator (the lambda), we're reversing that ordering
		Collections.sort(actuallySorted, 
				(val1, val2) -> -1*Integer.compare(val1, val2));
		
		Assert.assertEquals(10, shouldBeSorted.size());
		Assert.assertEquals(actuallySorted, shouldBeSorted);
	}
}
