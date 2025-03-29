package api;

import java.util.List;

public class ComputeResponseImpl implements ComputeResult {
    private final boolean success;
    private final String errorMessage;

    public ComputeResponseImpl(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public List<Integer> getFactors() {
        // Since this class represents a response rather than actual computation results,
        // we can return null or an empty list if success is false
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
