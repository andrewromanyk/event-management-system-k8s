package ua.edu.ukma.event_management_micro.ticket;

import org.apache.catalina.core.ApplicationContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.edu.ukma.event_management_micro.core.BuildingDto;
import ua.edu.ukma.event_management_micro.core.LogEvent;
import ua.edu.ukma.event_management_micro.event.api.EventApi;
import ua.edu.ukma.event_management_micro.user.api.UserApi;


import java.util.List;
import java.util.Optional;


@Service
public class TicketService {

    private ModelMapper modelMapper;
    private UserApi userInterface;
    private EventApi eventApi;
    private TicketRepository ticketRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    private RestTemplate restTemplate;

    @Value("${building.service.url}")
    private String buildingServiceUrl;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setEventApi(EventApi eventApi) {
        this.eventApi = eventApi;
    }

    @Autowired
    public void setUserInterface(UserApi userInterface) {
        this.userInterface = userInterface;
    }

    @Autowired
    void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public boolean createTicket(TicketDto ticket) {
        TicketEntity ticketEntity = modelMapper.map(ticket, TicketEntity.class);

        if (!(userInterface.validateUserExists(ticketEntity.getOwner()) || eventApi.eventExists(ticket.getEvent()))) {
            return false;
        }

        Integer amount = ticketRepository.countAllByEvent(ticketEntity.getEvent());

        Integer capacity = buildingCapacity(eventApi.getBuildingId(ticketEntity.getEvent()));

        if (amount >= capacity) {
            return false;
        }

        ticketRepository.save(ticketEntity);

        applicationEventPublisher.publishEvent(new LogEvent(this, "New ticket created: " + ticketEntity.getId()));

        return true;
    }

    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(a -> modelMapper.map(a, TicketDto.class))
                .toList();
    }

    public Optional<TicketDto> getTicketById(long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(a -> modelMapper.map(a, TicketDto.class));
    }

    public void removeTicket(long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    public List<TicketDto> getAllTicketsForUser(Long user) {
        return ticketRepository
                .findAllByOwner(user)
                .stream()
                .map(a -> modelMapper.map(a, TicketDto.class)).toList();
    }

//    public List<TicketDto> getAllTicketsCreatedByUser(long user) {
//        return ticketRepository
//                .findTicketEntitiesByEvent_Creator_Id(user)
//                .stream()
//                .map(a -> modelMapper.map(a, TicketDto.class))
//                .toList();
//    }


    public int buildingCapacity(long buildingId) {
        try {
            ResponseEntity<BuildingDto> response = restTemplate.getForEntity(
                    buildingServiceUrl + "/api/building/" + buildingId,
                    BuildingDto.class
            );
            return response.getBody().getCapacity();
        } catch (Exception e) {
            return 0;
        }
    }

}
