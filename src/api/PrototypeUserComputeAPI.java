package api;

import project.annotations.NetworkAPIPrototype;

// Implement the prototype class
@NetworkAPIPrototype
public class PrototypeUserComputeAPI implements UserComputeAPI {
    private String inputSource = "default_input";
    private String outputDestination = "default_output";
    private String delimiters = ",";

    @Override
    public void setInputSource(String source) {
        this.inputSource = source;
    }

    @Override
    public void setOutputDestination(String destination) {
        this.outputDestination = destination;
    }

    @Override
    public void setDelimiters(String delimiters) {
        this.delimiters = delimiters;
    }

    @Override
    public String processRequest() {
        return "Processing request with prototype";
    }

    // Method to test if updates work correctly
    public String getStatus() {
        return "InputSource: " + inputSource + ", OutputDestination: " + outputDestination + ", Delimiters: " + delimiters;
    }

    // Main method for quick testing
    public static void main(String[] args) {
        PrototypeUserComputeAPI api = new PrototypeUserComputeAPI();
        System.out.println(api.processRequest()); // Should print: Processing request with prototype

        api.setInputSource("user_input");
        api.setOutputDestination("user_output");
        api.setDelimiters(";");

        System.out.println(api.getStatus()); // Should reflect updated values
    }
}
