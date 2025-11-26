package ua.edu.ukma.event_management_micro.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/building")
public class BuildingControllerApi {

    private BuildingService buildingService;

    @Autowired
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingDto> getBuilding(@PathVariable long id) {
        return ResponseEntity.of(buildingService.getBuildingById(id));
    }

    @GetMapping
    public List<BuildingDto> getBuildings(
        @RequestParam(required = false) Integer capacity
    ) {
        if (capacity == null) {
            return buildingService.getAllBuildings()
                    .stream()
                    .toList();
        } else {
            return buildingService.getAllByCapacity(capacity)
                    .stream()
                    .toList();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createNewBuilding(@RequestBody BuildingDto buildingDto,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        BuildingDto returned = buildingService.createBuilding(buildingDto);
        return new ResponseEntity<>(returned, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBuilding(@PathVariable long id, @RequestBody BuildingDto buildingDto,
                                            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        buildingService.updateBuilding(id, buildingDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable long id) {
        buildingService.deleteBuilding(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}