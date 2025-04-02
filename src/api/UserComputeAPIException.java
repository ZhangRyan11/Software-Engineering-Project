package api;

import project.annotations.UserComputeAPIPrototype;
import api.ValidationException;

public class UserComputeAPIException extends UserComputeAPIPrototype {

    @Override
    public void setInputSource(String source) {
        try {
            super.setInputSource(source);
        } catch (ValidationException e) {
            this.validationError = e.getMessage();
        }
    }
    
    // Add a field to store validation errors
    private String validationError = null;
    
    // Override to catch and report any validation exceptions
    
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
