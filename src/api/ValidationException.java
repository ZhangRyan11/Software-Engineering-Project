package api;

/**
 * Exception thrown when validation errors occur in the API.
 */
public class ValidationException extends Exception {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
