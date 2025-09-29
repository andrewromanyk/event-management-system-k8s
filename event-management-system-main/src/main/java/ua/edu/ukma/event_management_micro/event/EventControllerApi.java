package ua.edu.ukma.event_management_micro.event;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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