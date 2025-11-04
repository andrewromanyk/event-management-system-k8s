package ua.edu.ukma.event_management_micro.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/ticket")
public class TicketControllerApi {

	private TicketService ticketService;

	@Autowired
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<TicketDto> getTicket(@PathVariable long id) {
		return ResponseEntity.ofNullable(ticketService.getTicketById(id).orElse(null));
	}

	@GetMapping
	public List<TicketDto> getTickets() {
		return ticketService.getAllTickets();
	}

	@GetMapping("/owner/{userId}")
	public List<TicketDto> getTicketsOwnedByUser(@PathVariable Long userId) {
		return ticketService.getAllTicketsForUser(userId);
	}

	@GetMapping("/events")
	public List<TicketDto> getTicketsForEvents(@RequestBody List<Long> eventIds) {
		return ticketService.getAllTicketsForEvents(eventIds);
	}

	@PostMapping
	public ResponseEntity<String> createTicket(@RequestBody TicketDto ticketDto) {
		if (ticketService.createTicket(ticketDto)) {
			return ResponseEntity.ok("Ticket created");
		}
		return ResponseEntity.badRequest().body("Ticket creation failed");
	}

	@DeleteMapping("/{id}")
	public void deleteTicket(@PathVariable long id) {
		ticketService.removeTicket(id);
	}

	@DeleteMapping("/return/{id}")
	public void returnTicket(@PathVariable long id) {
		ticketService.returnTicket(id);
	}
}
