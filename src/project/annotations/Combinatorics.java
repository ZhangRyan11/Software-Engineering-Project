package project.annotations;

public class Combinatorics {
    
    public long permutation(int n, int r) {
        if (n < r || n < 0 || r < 0) {
            throw new IllegalArgumentException("Invalid input: n must be >= r and both must be non-negative");
        }
        return Math.round(factorialDouble(n) / factorialDouble(n - r));
    }
    
    public long combination(int n, int r) {
        if (n < r || n < 0 || r < 0) {
            throw new IllegalArgumentException("Invalid input: n must be >= r and both must be non-negative");
        }
        return Math.round(factorialDouble(n) / (factorialDouble(r) * factorialDouble(n - r)));
    }
    
    private double factorialDouble(int n) {
        if (n == 0 || n == 1) {
            return 1.0;
        }
        return n * factorialDouble(n - 1);
    }
}
