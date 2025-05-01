package api;

import java.util.List;

public class ComputationResultImpl implements ComputationResult {
    private final boolean success;
    private final List<Integer> factors;
    private final String message;

    public ComputationResultImpl(boolean success, List<Integer> factors) {
        this.success = success;
        this.factors = factors;
        this.message = success ? "Operation completed successfully" : "Operation failed";
    }

    public ComputationResultImpl(boolean success, List<Integer> factors, String message) {
        this.success = success;
        this.factors = factors;
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public List<Integer> getFactors() {
        return factors;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
