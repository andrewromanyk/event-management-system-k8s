package ua.edu.ukma.event_management_micro.event.api;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.ukma.event_management_micro.event.EventService;

@Component
public class EventApiIml implements EventApi {

    private final EventService eventService;

    @Autowired
    public EventApiIml(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean eventExists(Long eventId) {
        return eventService.eventExists(eventId);
    }

    @Override
    public Long getBuildingId(Long eventId) {
        return eventService.getEventById(eventId).getBuildingId();
    }

}
