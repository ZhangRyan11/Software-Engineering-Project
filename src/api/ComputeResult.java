package api;

public class ComputeResult{
    private String message;

    public ComputeResult(boolean b, String message) {
        this.message = message;
    }

    public ComputeResult(boolean b, Object message2) {
        
	}

	public enum ComputeResultStatus {
        SUCCESS(true), FAILURE(false), INVALID_REQUEST(false);
        public boolean isSuccess;
        private ComputeResultStatus(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }
    }
    public String getMessage() {
        return message;
    }
}

