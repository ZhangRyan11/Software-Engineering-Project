package api;

class ComputeResponseImpl implements ComputeResponse {
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
    public String getErrorMessage() {
        return errorMessage;
    }
}
