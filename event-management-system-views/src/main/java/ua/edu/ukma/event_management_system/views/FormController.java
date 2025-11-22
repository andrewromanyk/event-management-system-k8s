package ua.edu.ukma.event_management_system.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import ua.edu.ukma.event_management_system.clients.EventClient;
import ua.edu.ukma.event_management_system.dto.EventDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FormController {

    private EventClient eventClient;

    @Autowired
    public void setEventClient(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    @GetMapping("/main")
    public String mainPage(Model model) throws IOException {
        List<EventDto> events = eventClient.getAllRelevantEvents();

        model.addAttribute("events", events);
        return "main";
    }
}