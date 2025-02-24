package exercise4;

import java.util.List;

public class FindMaximumValue {

	public int findMaximumValue(List<Integer> values) {
		int currentMax = Integer.MIN_VALUE;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) > currentMax) {
				currentMax = values.get(i);
			}
		}
		return currentMax;
	}
}
