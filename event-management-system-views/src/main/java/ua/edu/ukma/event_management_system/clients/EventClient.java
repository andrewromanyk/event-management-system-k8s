package ua.edu.ukma.event_management_system.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.dto.EventDto;

import javax.management.DescriptorKey;
import java.util.List;

@FeignClient(name = "event-client", url = "${feign.client.event.url}")
public interface EventClient {

    @GetMapping("/api/event/{id}")
    EventDto getEventById(@PathVariable Long id);

    @GetMapping("/api/event/")
    List<EventDto> getAllEvents();

    @GetMapping("/api/event/relevant")
    List<EventDto> getAllRelevantEvents();

    @GetMapping("/api/event/organizer/{organizerId}")
    List<EventDto> getEventsForOrganizer(@PathVariable Long organizerId);

    @GetMapping("/api/event/intersect")
    List<EventDto> getEventsIntersect(
            @RequestParam("start") String startIso,
            @RequestParam("end") String endIso,
            @RequestParam("buildingId") long buildingId
    );

    @PostMapping("/api/event")
    String createNewEvent(EventDto eventDto);

    @PutMapping("/api/event/{id}")
    String updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto);

    @DeleteMapping("/api/event/{id}")
    void deleteEvent(@PathVariable Long id);

}
