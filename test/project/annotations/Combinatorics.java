package exercise3;

/**
 * Supports common combinatorics calculations:
 * permutation(n, k) = n!/(n-k)!
 * combination(n, k) = n!/(k!*(n-k)!)
 */
public class Combinatorics {

	public int permutation(int n, int k) {
		int numerator = factorial(n);
		int denominator = factorial(n - k);
		return numerator / denominator;
	}
	
	public int combination(int n, int k) {
		int numerator = factorial(n);
		int denominator = factorial(n - k)*factorial(k);
		return numerator / denominator;
	}
	
	// Returns the product of all numbers from 1 to n
	// Ex: 3! = factorial(3) = 1 * 2 * 3 = 6
	private int factorial(int n) {
		int product = 1;
		for (int i = 0; i < n; i++) {
			product *= i;
		}
		return product;
	}

}
