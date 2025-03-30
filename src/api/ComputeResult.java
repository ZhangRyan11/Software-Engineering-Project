package api;

public class ComputeResult{
    private ComputeResultStatus status;
    private String message;

    public ComputeResult(ComputeResultStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public enum ComputeResultStatus {
        SUCCESS(true), FAILURE(false), INVALID_REQUEST(false);
        public boolean isSuccess;
        private ComputeResultStatus(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }
    }

    public boolean isSuccess() {
        return status.isSuccess;
    }
    public String getMessage() {
        return message;
    }
}

