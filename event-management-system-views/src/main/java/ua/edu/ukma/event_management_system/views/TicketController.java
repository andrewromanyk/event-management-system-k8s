package ua.edu.ukma.event_management_system.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.clients.EventClient;
import ua.edu.ukma.event_management_system.clients.TicketClient;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("ticket")
public class TicketController {

	private UserClient userClient;
	private TicketClient ticketClient;
	private EventClient eventClient;

	@Autowired
	public void setTicketClient(TicketClient ticketClient) {
		this.ticketClient = ticketClient;
	}

	@Autowired
	public void setUserClient(UserClient userClient) {
		this.userClient = userClient;
	}

	@Autowired
	public void setEventClient(EventClient eventClient) {
		this.eventClient = eventClient;
	}

	@GetMapping("/")
	public String getAll(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto user = userClient.getUser(userDetails.getUsername()).getBody();
		List<TicketDto> tickets = switch (user.getUserRole()) {
			case ADMIN -> ticketClient.getAllTickets();
			case ORGANIZER -> {
				List<Long> eventIds = eventClient.getEventsForOrganizer(user.getId())
						.stream()
						.map(EventDto::getId)
						.toList();
				yield ticketClient.getTicketsForEvents(eventIds);
			}
			case USER -> ticketClient.getTicketsForUser(user.getId());
		};

		List<EventDto> events = eventClient.getAllEvents();
		List<UserDto> users = userClient.getAllUsers();
		Map<Long, EventDto> eventsMap = events.stream().collect(
				java.util.stream.Collectors.toMap(EventDto::getId, event -> event));
		Map<Long, UserDto> usersMap = users.stream().collect(
				java.util.stream.Collectors.toMap(UserDto::getId, u -> u));


		model.addAttribute("users", usersMap);
		model.addAttribute("events", eventsMap);
		model.addAttribute("tickets", tickets);
		return "tickets/ticket-list";
	}

	@GetMapping("/{id}")
	public String delete(@PathVariable long id) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto currentUser = userClient.getUser(userDetails.getUsername()).getBody();

		TicketDto ticketToDelete = ticketClient.getTicketById(id);

		if (Objects.equals(currentUser.getId(), ticketToDelete.getOwner())
				|| currentUser.getUserRole() == UserRole.ADMIN) { //the deleter is admin
			ticketClient.deleteTicket(id);
		}

		return "redirect:/ticket/";
	}

	@PostMapping("/")
	public String create(@ModelAttribute TicketDto ticket) {
		ticket.setPurchaseDate(LocalDateTime.now());
		ticketClient.createTicket(ticket);
		return "redirect:/ticket/";
	}
}
