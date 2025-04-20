package api;

import project.annotations.NetworkAPIPrototype;
import project.annotations.ValidationException;

@NetworkAPIPrototype
public class PrototypeUserComputeAPI implements UserComputeAPI {
    private String inputSource = "default_input";
    private String outputDestination = "default_output";
    private String delimiters = ",";

    @Override
    public void setInputSource(String source) throws ValidationException {
        if (source == null || source.trim().isEmpty()) {
            throw new ValidationException("Input source cannot be null or empty");
        }
        this.inputSource = source;
    }

    @Override
    public void setOutputDestination(String destination) throws ValidationException {
        if (destination == null || destination.trim().isEmpty()) {
            throw new ValidationException("Output destination cannot be null or empty");
        }
        this.outputDestination = destination;
    }

    @Override
    public void setDelimiters(String delimiters) {
        this.delimiters = delimiters != null ? delimiters : ",";
    }

    @Override
    public String processRequest() {
        // Intentionally slow processing for benchmark comparison
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            result.append(inputSource).append(i);
            result.append(outputDestination).append(i);
            try {
                Thread.sleep(5); // Add small delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return result.toString();
    }

    // Method to test if updates work correctly
    public String getStatus() {
        return "InputSource: " + inputSource + ", OutputDestination: " + outputDestination + ", Delimiters: " + delimiters;
    }

    // Main method for quick testing
    public static void main(String[] args) {
        PrototypeUserComputeAPI api = new PrototypeUserComputeAPI();
        try {
            api.setInputSource("user_input");
            api.setOutputDestination("user_output");
        } catch (ValidationException e) {
            System.err.println(e.getMessage());
        }
        api.setDelimiters(";");

        System.out.println(api.getStatus()); // Should reflect updated values
        System.out.println(api.processRequest()); // Should print processed request
    }
}
