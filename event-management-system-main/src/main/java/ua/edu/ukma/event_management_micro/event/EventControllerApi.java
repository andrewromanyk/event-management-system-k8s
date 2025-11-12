package ua.edu.ukma.event_management_micro.event;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/event")
public class EventControllerApi {

    private ModelMapper modelMapper;
    private EventService eventService;

    @Autowired
    public void setModelWrapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setEventService(EventService eventService){
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable long id){
        return eventService.getEventById(id);
    }

    @GetMapping("/")
    public List<EventDto> getEvents(@RequestParam(required = false) String title){
        if(title == null) {
            return eventService.getAllEvents().stream().toList();
        }else {
            return eventService.getAllEventsByTitle(title).stream().toList();
        }
    }

    @GetMapping("/organizer/{organizerId}")
    public List<EventDto> getEventsForOrganizer(@PathVariable long organizerId){
        return eventService.getAllForOrganizer(organizerId).stream().toList();
    }

    @GetMapping("/relevant")
    public List<EventDto> getRelevantEventsForUser(){
        return eventService.getAllRelevant().stream().toList();
    }

    // using event servcie getAllThatIntersect method
    @GetMapping("/intersect")
    public ResponseEntity<List<EventDto>> getEventsIntersect(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam("buildingId") long buildingId
    ) {
        if (start.isAfter(end)) {
            return ResponseEntity.badRequest().build();
        }
        List<EventDto> events = eventService.getAllThatIntersect(start, end, buildingId);
        return ResponseEntity.ok(events);
    }


    @PostMapping
    public ResponseEntity<String> createNewEvent(@RequestBody EventDto eventDto){
        eventService.createEvent(eventDto);

        return ResponseEntity.ok("Event created");
    }

    @PutMapping("/{id}")
    public void updateEvent(@PathVariable long id, @RequestBody EventDto eventDto){
        eventService.updateEvent(id, eventDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable long id) {
        eventService.deleteEvent(id);
    }

}