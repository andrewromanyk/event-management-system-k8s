package ua.edu.ukma.event_management_micro.event;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_micro.core.dto.LogEvent;
import ua.edu.ukma.event_management_micro.grpc.BuildingGrpcClient;
import ua.edu.ukma.event_management_micro.user.api.UserApi;
import ua.edu.ukma.event_management_system_building.grpc.Building;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EventService {

    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;
    private final UserApi userApi;
    private ApplicationEventPublisher applicationEventPublisher;

    private BuildingGrpcClient buildingGrpcClient;

    @Autowired
    public void setBuildingGrpcClient(BuildingGrpcClient buildingGrpcClient) {
        this.buildingGrpcClient = buildingGrpcClient;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Autowired
    public EventService(ModelMapper modelMapper, EventRepository eventRepository,
                        UserApi userApi) {
        this.modelMapper = modelMapper;
        this.eventRepository = eventRepository;
        this.userApi = userApi;
    }

    public List<EventDto> getAllEvents(){
        return eventRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public EventDto getEventById(long eventId) {
        return toDomain(eventRepository.findById(eventId)
                .orElseThrow());
    }

    public List<EventDto> getAllEventsByTitle(String title) {
        return eventRepository.findAllByEventTitle(title)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public void createEvent(EventDto event) {
        // Ensures the building with such id exists before saving the event
        EventEntity toSave = toEntity(event);
        Long buildingId = event.getBuildingId();
        Long creatorId = event.getCreatorId();
        if (buildingId != null
                && creatorId != null
//                && buildingExists(buildingId)
                && userApi.validateUserExists(creatorId)) {
            eventRepository.save(toSave);
        } else {
            throw new NoSuchElementException("Smt wrong with building or user");
        }

        applicationEventPublisher.publishEvent(new LogEvent(this, "New event created: " + toSave.getEventTitle()));
    }

    public void updateEvent(Long id, EventDto updatedEvent) {
        Optional<EventEntity> existingEventOpt = eventRepository.findById(id);

        if (existingEventOpt.isPresent()) {
            Long buildingId = updatedEvent.getBuildingId();
            Long creatorId = updatedEvent.getCreatorId();
            if (buildingId != null
                    && creatorId != null
//                    && buildingExists(buildingId)
                    && userApi.validateUserExists(creatorId)
            ) {
                EventEntity existingEvent = existingEventOpt.get();
                existingEvent.setEventTitle(updatedEvent.getEventTitle());
                existingEvent.setDateTimeStart(updatedEvent.getDateTimeStart());
                existingEvent.setDateTimeEnd(updatedEvent.getDateTimeEnd());
                existingEvent.setDescription(updatedEvent.getDescription());
                existingEvent.setNumberOfTickets(updatedEvent.getNumberOfTickets());
                existingEvent.setMinAgeRestriction(updatedEvent.getMinAgeRestriction());
                existingEvent.setPrice(updatedEvent.getPrice());
                existingEvent.setDescription(updatedEvent.getDescription());
                existingEvent.setNumberOfTickets(updatedEvent.getNumberOfTickets());
                existingEvent.setMinAgeRestriction(updatedEvent.getMinAgeRestriction());

                eventRepository.save(existingEvent);
            }
        } else {
            throw new NoSuchElementException("Smt wrong with building or user");
        }
    }

    public void deleteEvent(long eventId) {
        eventRepository.deleteById(eventId);
    }

    public List<EventDto> getAllRelevant() {
        return eventRepository.findEventEntitiesByDateTimeEndAfter(LocalDateTime.now())
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<EventDto> getAllForOrganizer(Long organizerId) {
        return eventRepository.findEventEntitiesByCreatorId(organizerId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<EventDto> getAllThatIntersect(LocalDateTime start, LocalDateTime end, long id) {
        return eventRepository.findEventEntitiesByBuildingIdAndDateTimeEndAfterAndDateTimeStartBefore(id, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public boolean eventExists(long eventId) {
        return eventRepository.existsById(eventId);
    }

    private EventDto toDomain(EventEntity event){
        return modelMapper.map(event, EventDto.class);
    }

    private EventEntity toEntity(EventDto event){
        return modelMapper.map(event, EventEntity.class);
    }

    private boolean buildingExists(long buildingId) {
        try {
            return buildingGrpcClient.streamAllBuildings().stream()
                    .map(Building::getId)
                    .anyMatch(id -> id == buildingId);
        }
        catch (Exception _) {
            return false;
        }
    }

    @PostConstruct
    public void testData() {
        //generate 1 event for first building assuming it exists
        if (eventRepository.count() == 0) {
            EventDto event = new EventDto();
            event.setEventTitle("Sample Event");
            event.setBuildingId(1L);
            event.setCreatorId(3L);
            event.setDateTimeStart(LocalDateTime.now().plusDays(1));
            event.setDateTimeEnd(LocalDateTime.now().plusDays(1).plusHours(2));
            event.setDescription("This is a sample event for testing purposes.");
            event.setNumberOfTickets(100);
            event.setMinAgeRestriction(18);
            event.setPrice(50.0);
            eventRepository.save(toEntity(event));
        }
    }
}