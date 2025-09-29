package ua.edu.ukma.event_management_micro.ticket;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.NamedInterface;

@ApplicationModule(allowedDependencies = {
        "event::api",
        "user::api"
})
@NamedInterface("api")
public class TicketModule {
}
