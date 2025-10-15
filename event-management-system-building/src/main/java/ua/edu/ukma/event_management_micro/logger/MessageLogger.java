package ua.edu.ukma.event_management_micro.logger;

import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ua.edu.ukma.event_management_micro.core.dto.LogEvent;
import ua.edu.ukma.event_management_micro.core.dto.TicketReturnDto;

@Component
public class MessageLogger {

    Logger logger = org.slf4j.LoggerFactory.getLogger(MessageLogger.class);

    @EventListener
    @Async
    public void handleLogEvent(LogEvent event) {
        logger.info(event.getLogMessage());
    }

    @JmsListener(
            destination = "return.ticket",
            containerFactory = "jmsContainerFactoryPubSubCustom")
    public void receiveTicketReturnMessage(TicketReturnDto ticketReturnDto) {
        logger.info("Ticket returned: {}", ticketReturnDto);
    }

}
