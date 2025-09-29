package ua.edu.ukma.event_management_micro.logger;

import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ua.edu.ukma.event_management_micro.core.LogEvent;

@Component
public class MessageLogger {

    Logger logger = org.slf4j.LoggerFactory.getLogger(MessageLogger.class);

    @EventListener
    @Async
    public void handleLogEvent(LogEvent event) {
        logger.info(event.getLogMessage());
    }

}
