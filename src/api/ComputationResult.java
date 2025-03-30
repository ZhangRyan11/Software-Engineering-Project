package api;

import java.util.List;

class ComputationResultImpl implements ComputationResult {
public class ComputationResultImpl implements ComputationResult {
    private final boolean success;
    private final List<Integer> factors;

    public ComputationResultImpl(boolean success, List<Integer> factors) {
        this.success = success;
        this.factors = factors;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Integer> getFactors() {
        return factors;
    }
}
