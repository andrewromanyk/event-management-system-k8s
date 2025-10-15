package ua.edu.ukma.event_management_micro.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ua.edu.ukma.event_management_micro.core.TicketReturnDto;

@Component
public class JmsListenerEmailSender {

    private final EmailService emailService;

    @Autowired
    public JmsListenerEmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(
            destination = "send.email",
            containerFactory = "jmsContainerFactoryQueueCustom")
    public void receiveMessage(EmailDto email) {
        emailService.sendEmail(email);
    }

    @JmsListener(
            destination = "return.ticket",
            containerFactory = "jmsContainerFactoryPubSubCustom",
            selector = "sendEmail = true")
    public void receiveTicketReturnMessage(TicketReturnDto ticketReturnDto) {
        String subject = "Ticket Return Confirmation";
        String text = String.format("Your ticket with ID %d has been successfully returned.",
                ticketReturnDto.getTicketId());
        EmailDto emailDto = new EmailDto("", ticketReturnDto.getUserEmail(), subject, text);
        emailService.sendEmail(emailDto);
    }
}
