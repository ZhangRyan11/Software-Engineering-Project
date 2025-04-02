package api;

import project.annotations.UserComputeAPIPrototype;

/**
 * Implementation of UserComputeAPIPrototype that specifically handles exceptions
 */
public class UserComputeAPIException extends UserComputeAPIPrototype {
    
    /**
     * Override to handle ValidationException internally rather than propagating
     */
    @Override
    public void setInputSource(String source) {
        try {
            super.setInputSource(source);
        } catch (ValidationException e) {
            // Silently catch the exception to be reported by processRequest
        }
    }
    
    /**
     * Override to catch and report any validation exceptions
     */
    @Override
    public String processRequest() {
        try {
            return super.processRequest();
        } catch (Exception e) {
            return "ERROR: Exception occurred: " + e.getMessage();
        }
    }
}
