package ua.edu.ukma.event_management_micro.event.api;

public interface EventApi {

    public boolean eventExists(Long eventId);
    Long getBuildingId(Long eventId);
}
