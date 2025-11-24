package ua.edu.ukma.event_management_system.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.dto.BuildingDto;

import java.util.List;

@FeignClient(name = "building-client", url = "${feign.client.building.url}")
public interface BuildingClient {

    @GetMapping("/api/building/{id}")
    ResponseEntity<BuildingDto> getBuildingById(@PathVariable Long id);

    @GetMapping("/api/building")
    List<BuildingDto> getAllBuildings();

    @PostMapping("/api/building")
    ResponseEntity<Void> createNewBuilding(@RequestBody BuildingDto buildingDto);

    @PutMapping("/api/building/{id}")
    ResponseEntity<Void> updateBuilding(@PathVariable Long id, @RequestBody BuildingDto buildingDto);

    @DeleteMapping("/api/building/{id}")
    ResponseEntity<Void> deleteBuilding(@PathVariable Long id);

}
