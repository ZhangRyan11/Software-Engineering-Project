package project.annotations;

public class Combinatorics {
    
    public long permutation(int n, int r) {
        return factorial(n) / factorial(n - r);
    }
    
    public long combination(int n, int r) {
        return factorial(n) / (factorial(r) * factorial(n - r));
    }
    
    private long factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
