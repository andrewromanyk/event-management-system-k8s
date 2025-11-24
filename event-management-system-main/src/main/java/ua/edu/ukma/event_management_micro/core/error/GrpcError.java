package ua.edu.ukma.event_management_micro.core.error;

public class GrpcError extends RuntimeException {
    public GrpcError(String message) {
        super(message);
    }
    public GrpcError(String message, Throwable cause) {
        super(message, cause);
    }
}
