package ua.edu.ukma.event_management_system.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ua.edu.ukma.event_management_system.clients.BuildingClient;
import ua.edu.ukma.event_management_system.dto.BuildingDto;
import jakarta.validation.Valid;

import java.util.*;

@Controller
@RequestMapping("building")
public class BuildingController {

    private static final String REDIRECT_BUILDING = "redirect:/building/";
    private static final String ERRORS = "errors";
    private static final String ERROR = "error";

    private BuildingClient buildingClient;

    @Autowired
    public void setBuildingService(BuildingClient buildingClient) {
        this.buildingClient = buildingClient;
    }

    @GetMapping("/{id}")
    public String getBuilding(@PathVariable long id, Model model) {
        BuildingDto building = buildingClient.getBuildingById(id).getBody();

        model.addAttribute("building", building);

        return "buildings/building-details";
    }

    @GetMapping("/")
    public String getBuildings(@RequestParam(required = false) Integer capacity, Model model) {
        List<BuildingDto> buildings = (capacity == null) ?
                buildingClient.getAllBuildings() :
                buildingClient.getAllBuildings().stream()
                        .filter(b -> b.getCapacity() >= capacity)
                        .toList();

        model.addAttribute("buildings", buildings);
        return "buildings/building-list";
    }

    @PostMapping("/")
    public String createNewBuilding(@ModelAttribute("buildingDto") @Valid BuildingDto buildingDto,
                                    BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String errorMessage = "Error for " + error.getField() + " field: " + error.getDefaultMessage();
                errors.add(errorMessage);
            });

            model.addAttribute(ERRORS, errors);
            return ERROR;
        }

        if(buildingDto.getDescription() == null || buildingDto.getDescription().isEmpty()){
            RestClient client = RestClient.create();
            String defaultDescription = client.get()
                    .uri("https://baconipsum.com/api/?type=meat-and-filler&sentences=2&format=text")
                    .retrieve()
                    .body(String.class);
            buildingDto.setDescription(defaultDescription);
        }

        buildingClient.createNewBuilding(buildingDto);
        return REDIRECT_BUILDING;
    }

    @GetMapping("/create")
    public String createBuildingForm(Model model) {
        model.addAttribute("buildingDto", new BuildingDto());
        return "buildings/building-form";
    }

    @GetMapping("/{id}/edit")
    public String editBuildingForm(@PathVariable long id, Model model){
        BuildingDto buildingDto = buildingClient.getBuildingById(id).getBody();
        model.addAttribute("buildingDto", buildingDto);
        return "buildings/building-form";
    }

    @PutMapping("/{id}")
    public String updateBuilding(@PathVariable long id, @Valid BuildingDto buildingDto,
                                            BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String errorMessage = "Error for " + error.getField() + " field: " + error.getDefaultMessage();
                errors.add(errorMessage);
            });
            model.addAttribute(ERRORS, errors);
            return ERROR;
        }
        buildingClient.updateBuilding(id, buildingDto);
        return REDIRECT_BUILDING;
    }

    @DeleteMapping("/{id}")
    public String deleteBuilding(@PathVariable long id) {
        buildingClient.deleteBuilding(id);
        return REDIRECT_BUILDING;
    }
}