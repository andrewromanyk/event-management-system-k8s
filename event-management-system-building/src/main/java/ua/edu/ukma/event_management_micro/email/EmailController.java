package ua.edu.ukma.event_management_micro.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ua.edu.ukma.event_management_micro.core.EmailDto;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-simple")
    public ResponseEntity<String> sendSimpleEmail(@RequestBody EmailDto emailDto) {
        try {
            emailService.sendSimpleEmail(emailDto.getTo(), emailDto.getSubject(), emailDto.getText());
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/demo")
    public ResponseEntity<String> sendDemoEmail(@RequestParam String to) {
        try {
            String subject = "Demo Email from Spring Boot";
            String message = "Hello! This is a demo email sent from your Spring Boot application at " +
                    java.time.LocalDateTime.now();

            emailService.sendSimpleEmail(to, subject, message);
            return ResponseEntity.ok("Demo email sent to " + to);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send demo email: " + e.getMessage());
        }
    }
}
