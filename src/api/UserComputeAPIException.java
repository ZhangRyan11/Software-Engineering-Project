package api;

import project.annotations.UserComputeAPIPrototype;

//Implementation of UserComputeAPIPrototype that specifically handles exceptions

public class UserComputeAPIException extends UserComputeAPIPrototype {
    
    // Add a field to store validation errors
    private String validationError = null;
    
    // Override to handle ValidationException internally rather than propagating
    
    @Override
    public void setInputSource(String source) {
        try {
            super.setInputSource(source);
        } catch (Exception e) {
            // Catch any exception, including ValidationException
            // Store the message to report it later
            this.validationError = e.getMessage();
        }
    }
    
    @Override
    public String processRequest() {
        // Check if we had a validation error during input setting
        if (validationError != null) {
            return "ERROR: Validation exception: " + validationError;
        }
        
        try {
            return super.processRequest();
        } catch (Exception e) {
            return "ERROR: Exception occurred: " + e.getMessage();
        }
    }
}
