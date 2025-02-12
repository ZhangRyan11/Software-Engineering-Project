package api;

public class WriteResponse {

    private WriteResponseStatus status;

    public WriteResponse() {
        this(WriteResponseStatus.SUCCESS);
    }

    public WriteResponse(WriteResponseStatus status) {
        this.status = status;
    }

    // Getter for status
    public WriteResponseStatus getStatus() {
        return status;
    }

    public static enum WriteResponseStatus {
        SUCCESS,
        FAILURE;
    }
}