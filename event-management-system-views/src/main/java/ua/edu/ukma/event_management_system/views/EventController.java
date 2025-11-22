package ua.edu.ukma.event_management_system.views;

import org.slf4j.Logger;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.edu.ukma.event_management_system.clients.BuildingClient;
import ua.edu.ukma.event_management_system.clients.EventClient;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.BuildingDto;
import ua.edu.ukma.event_management_system.dto.EventDto;
import ua.edu.ukma.event_management_system.dto.TicketDto;
import ua.edu.ukma.event_management_system.dto.UserDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("event")
public class EventController {

	private static final String DATA_IMAGE = "data:image/png;base64,";
	private static final String STOCK_PHOTO = "src/main/resources/stock_photo.jpg";
	private static final String EVENT_FORM = "events/event-form";
	private static final String EVENT = "event";
	private static final String ERROR = "error";
	private static final String REDIRECT_EVENT = "redirect:/event/";
	private static final String BUILDINGS = "buildings";

	private EventClient eventClient;
	private UserClient userClient;
	private BuildingClient buildingClient;

	private final Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	public void setEventClient(EventClient eventClient) {
		this.eventClient = eventClient;
	}

	@Autowired
	public void setUserClient(UserClient userClient) {
		this.userClient = userClient;
	}

	@Autowired
	public void setBuildingClient(BuildingClient buildingClient) {
		this.buildingClient = buildingClient;
	}

	@GetMapping("/")
	public String get(Model model) {
		UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto user = userClient.getUser(details.getUsername()).getBody();
		List<EventDto> events = switch (user.getUserRole()) {
            case ADMIN -> eventClient.getAllEvents();
            case ORGANIZER -> eventClient.getEventsForOrganizer(user.getId());
            case USER -> eventClient.getAllRelevantEvents();
        };

		model.addAttribute("events", events);
		return "events/events";
	}

	@GetMapping("/{id}")
	public String get(@PathVariable long id, Model model) throws IOException {
		EventDto event = eventClient.getEventById(id);
		UserDetails detail;
		UserDto user = null;
		try {
			detail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user = userClient.getUser(detail.getUsername()).getBody();
		} catch (Exception e) {
			log.info("User is not authorized.");
		}

		ResponseEntity<BuildingDto> response = buildingClient.getBuildingById(event.getBuildingId());

		BuildingDto building = response.getBody();

		model.addAttribute("building", building);
		model.addAttribute(EVENT, event);
		model.addAttribute("isAllowedToBuy", user == null || user.getAge() >= event.getMinAgeRestriction());
		return "events/event";
	}

	@GetMapping("/create")
	public String showCreateEventForm(Model model) {
		List<BuildingDto> buildings = buildingClient.getAllBuildings();
		model.addAttribute(BUILDINGS, buildings);
		model.addAttribute(EVENT, new EventDto());
		return EVENT_FORM;
	}

	@PostMapping("/create")
	public String createEvent(@Valid @ModelAttribute("event") EventDto eventDto,
							  BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute(EVENT, eventDto);
			List<BuildingDto> buildings = buildingClient.getAllBuildings();
			model.addAttribute(BUILDINGS, buildings);
			return EVENT_FORM;
		}
		if (eventDto.getDateTimeEnd().isBefore(eventDto.getDateTimeStart())) {
			model.addAttribute(EVENT, eventDto);
			model.addAttribute(ERROR, "Event cannot end before it started!");
			List<BuildingDto> buildings = buildingClient.getAllBuildings();
			model.addAttribute(BUILDINGS, buildings);
			return EVENT_FORM;
		}
		if (!eventClient.getEventsIntersect(eventDto.getDateTimeStart().toString(), eventDto.getDateTimeEnd().toString(), eventDto.getBuildingId()).isEmpty()) {
			model.addAttribute(EVENT, eventDto);
			model.addAttribute(ERROR, "Building is already occupied for that time");
			List<BuildingDto> buildings = buildingClient.getAllBuildings();
			model.addAttribute(BUILDINGS, buildings);
			return EVENT_FORM;
		}
		UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto user = userClient.getUser(details.getUsername()).getBody();
		eventDto.setCreatorId(user.getId());
		eventClient.createNewEvent(eventDto);
		return REDIRECT_EVENT;
	}

	@DeleteMapping("/{id}")
	public String deleteEvent(@PathVariable long id, RedirectAttributes redirectAttributes) {
		try {
			eventClient.deleteEvent(id);
			redirectAttributes.addFlashAttribute("successMessage", "Event successfully deleted.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the event. Please try again.");
		}
		return REDIRECT_EVENT;
	}

	@GetMapping("/{id}/buy-ticket")
	public String buyTicket(@PathVariable long id, Model model) {
		UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto user = userClient.getUser(details.getUsername()).getBody();

		EventDto event = eventClient.getEventById(id);

		TicketDto ticket = new TicketDto();
		ticket.setOwner(user.getId());
		ticket.setEvent(event.getId());
		ticket.setPrice(event.getPrice());

		model.addAttribute("ticket", ticket);
		model.addAttribute(EVENT, event);
		return "tickets/ticket-form";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable long id, Model model) {
		EventDto event = eventClient.getEventById(id);
		List<BuildingDto> buildings = buildingClient.getAllBuildings();
		String formattedDate1 = event.getDateTimeStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
		String formattedDate2 = event.getDateTimeEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
		model.addAttribute("dateTimeStartString", formattedDate1);
		model.addAttribute("dateTimeEndString", formattedDate2);
		model.addAttribute(BUILDINGS, buildings);
		model.addAttribute(EVENT, event);
		return EVENT_FORM;
	}

	@PutMapping("/{id}")
	public String editPut(@PathVariable long id, @ModelAttribute EventDto event, Model model) {
		if (event.getDateTimeEnd().isBefore(event.getDateTimeStart())) {
			model.addAttribute(EVENT, event);
			model.addAttribute(ERROR, "Event cannot end before it started!");
			List<BuildingDto> buildings = buildingClient.getAllBuildings();
			model.addAttribute(BUILDINGS, buildings);
			return EVENT_FORM;
		}
		List<EventDto> list = eventClient.getEventsIntersect(event.getDateTimeStart().toString(), event.getDateTimeEnd().toString(), event.getBuildingId());
		if (!list.isEmpty() && list.size() != 1 && list.get(0).getId() != id) {
			model.addAttribute(EVENT, event);
			model.addAttribute(ERROR, "Building is already occupied for that time");
			List<BuildingDto> buildings = buildingClient.getAllBuildings();
			model.addAttribute(BUILDINGS, buildings);
			return EVENT_FORM;
		}
		UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto user = userClient.getUser(details.getUsername()).getBody();
		event.setCreatorId(user.getId());
		eventClient.updateEvent(id, event);
		return REDIRECT_EVENT;
	}

}
