package api;

import project.annotations.NetworkAPIPrototype;

public class ComputationCoordinatorPrototype {
    
    // Inner interfaces/classes needed for the prototype
    public interface InputConfig {
        // Configuration for input will be defined in implementation
    }

    public interface OutputConfig {
        // Configuration for output will be defined in implementation
    }

    public static class ComputeRequest {
        private final InputConfig inputConfig;
        private final OutputConfig outputConfig;
        private final char delimiter;

        public ComputeRequest(InputConfig inputConfig, OutputConfig outputConfig, char delimiter) {
            this.inputConfig = inputConfig;
            this.outputConfig = outputConfig;
            this.delimiter = delimiter;
        }
    }

    public static class ComputeResult {
        private final Status status;

        public ComputeResult(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }
    }

    public enum Status {
        SUCCESS(true),
        FAILURE(false);

        private final boolean success;

        Status(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    @NetworkAPIPrototype
    public void prototype(ComputationCoordinator apiToCall) {
        InputConfig inputConfig = new InputConfig() {};
        OutputConfig outputConfig = null;
        
        ComputeRequest request = new ComputeRequest(inputConfig, outputConfig, ',');
        ComputeResult result = apiToCall.compute(request);
        
        if (result.getStatus().isSuccess()) {
            System.out.println("Yay!");
        }
    }

    // Interface that defines the computation coordinator
    public interface ComputationCoordinator {
        ComputeResult compute(ComputeRequest request);
    }
}
