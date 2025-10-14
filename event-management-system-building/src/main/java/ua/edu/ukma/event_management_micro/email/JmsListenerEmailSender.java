package ua.edu.ukma.event_management_micro.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsListenerEmailSender {

    private final EmailService emailService;

    @Autowired
    public JmsListenerEmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = "send.email", containerFactory = "myFactory")
    public void receiveMessage(EmailDto email) {
        emailService.sendEmail(email);
    }
}
