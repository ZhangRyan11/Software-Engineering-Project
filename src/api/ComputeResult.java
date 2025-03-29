package api;

import java.util.List;

public interface ComputeResult {
    boolean isSuccess();
    List<Integer> getFactors();
    String getErrorMessage();
    
    // Enum can be kept as a nested type within the interface
    enum ComputeResultStatus {
        SUCCESS(true), FAILURE(false), INVALID_REQUEST(false);
        public boolean isSuccess;
        private ComputeResultStatus(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }
    }
}

