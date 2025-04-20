package api;

public class ApiValidationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApiValidationException(String message) {
        super(message);
    }
}