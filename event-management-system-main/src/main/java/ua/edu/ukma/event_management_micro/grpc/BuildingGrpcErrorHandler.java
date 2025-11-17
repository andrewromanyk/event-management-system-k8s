package ua.edu.ukma.event_management_micro.grpc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BuildingGrpcErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handle(RuntimeException ex) {
        if (ex.getMessage().contains("not found")) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
        return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
    }
}
