package ua.edu.ukma.event_management_micro.logger;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        allowedDependencies = {
                "core"
        }
)
public class LoggerModule {
}
