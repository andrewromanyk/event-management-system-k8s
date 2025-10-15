package ua.edu.ukma.event_management_micro.ticket;

import jakarta.jms.ObjectMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_micro.core.dto.BuildingDto;
import ua.edu.ukma.event_management_micro.core.CoreService;
import ua.edu.ukma.event_management_micro.core.dto.EmailDto;
import ua.edu.ukma.event_management_micro.core.dto.LogEvent;
import ua.edu.ukma.event_management_micro.core.dto.TicketReturnDto;
import ua.edu.ukma.event_management_micro.event.api.EventApi;
import ua.edu.ukma.event_management_micro.user.api.UserApi;


import java.util.List;
import java.util.Optional;


@Service
public class    TicketService {

    private ModelMapper modelMapper;
    private UserApi userApi;
    private EventApi eventApi;
    private TicketRepository ticketRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    private CoreService coreService;
    private JmsTemplate jmsTemplate;
    private JmsTemplate jmsTopicTemplate;

    @Value("${building.service.url}")
    private String buildingServiceUrl;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setJmsTemplate(@Qualifier("jmsTemplate") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Autowired
    public void setJmsTopicTemplate(@Qualifier("jmsTopicTemplate") JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }
    @Autowired
    public void setCoreService(CoreService coreService) {
        this.coreService = coreService;
    }

    @Autowired
    public void setEventApi(EventApi eventApi) {
        this.eventApi = eventApi;
    }

    @Autowired
    public void setUserApi(UserApi userApi) {
        this.userApi = userApi;
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

        if (!(userApi.validateUserExists(ticketEntity.getOwner()) || eventApi.eventExists(ticket.getEvent()))) {
            return false;
        }

        Integer amount = ticketRepository.countAllByEvent(ticketEntity.getEvent());

        Integer capacity = buildingCapacity(eventApi.getBuildingId(ticketEntity.getEvent()));

        if (amount >= capacity) {
            return false;
        }

        ticketRepository.save(ticketEntity);

        applicationEventPublisher.publishEvent(new LogEvent(this, "New ticket created: " + ticketEntity.getId()));

        jmsTemplate.convertAndSend("send.email", new EmailDto("", userApi.getUserEmail(ticket.getOwner()), "Ticket purchase",
                "You have successfully purchased a ticket for event " + eventApi.getEventName(ticketEntity.getEvent()))
        );

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

    public void returnTicket(long ticketId) {
        Optional<TicketEntity> ticketEntity = ticketRepository.findById(ticketId);
        ticketEntity.ifPresent(ticket -> {
            ticketRepository.delete(ticket);
            jmsTopicTemplate.send("return.ticket", session -> {
                TicketReturnDto ticketReturnDto =
                        new TicketReturnDto(ticket.getId(), ticket.getEvent(), ticket.getOwner(), userApi.getUserEmail(ticket.getOwner()), "User requested ticket return");
                ObjectMessage message = session.createObjectMessage();
                message.setObject(ticketReturnDto);

                message.setBooleanProperty("sendEmail", true);

                return message;
            });
        });
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
            return ((BuildingDto) coreService
                    .callWithToken(buildingServiceUrl + "/api/building/" + buildingId, HttpMethod.GET, BuildingDto.class)
                    .getBody())
                    .getCapacity();
        } catch (Exception _) {
            return 0;
        }
    }

}
