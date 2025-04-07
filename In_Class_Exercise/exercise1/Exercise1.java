package exercise1;

import java.util.List;

public class Exercise1 {

	/** 
	 * This method takes the product of each element in array1 with each element in array2,
	 * sums them all together, and returns that result.
	 * 
	 * Assuming you've discovered that this method is a bottleneck in your code, come up with an alternative way to
	 * write the code that may be faster
	 */
	public int thisMethodIsTooSlow(List<Integer> array1, List<Integer> array2) {
		int vectorProduct = 0;
		
		int sumArray2=0;
		for (int j = 0; j < array1.size(); j++) {
			sumArray1 = array1.get(i)
		}
			for (int i = 0; i < array2.size(); i++) {
				vectorProduct += array1.get(i)*sumArray2;
			}
		}
		return vectorProduct;
	}
}
