package ua.edu.ukma.event_management_system.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.dto.TicketDto;

import java.util.List;

@FeignClient(name = "ticket-client", url = "${feign.client.ticket.url}")
public interface TicketClient {

    // get ticket by id
    @GetMapping("/api/ticket/{id}")
    TicketDto getTicketById(@PathVariable Long id);

    // get all tickets
    @GetMapping("/api/ticket")
    List<TicketDto> getAllTickets();

    // all tickets owned by user
    @GetMapping("/api/ticket/owner/{userId}")
    List<TicketDto> getTicketsForUser(@PathVariable Long userId);

    // get all tickets for events
    @PostMapping("/api/ticket/events")
    List<TicketDto> getTicketsForEvents(@RequestBody List<Long> eventIds);

    @PostMapping("/api/ticket")
    String createTicket(@RequestBody TicketDto ticketDto);

    @DeleteMapping("/api/ticket/return/{id}")
    void deleteTicket(@PathVariable Long id);
}
