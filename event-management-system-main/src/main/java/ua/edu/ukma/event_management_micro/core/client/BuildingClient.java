package ua.edu.ukma.event_management_micro.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_micro.core.dto.BuildingDto;

import java.util.List;

@FeignClient(name = "building-client", url = "${feign.client.building.url:http://localhost:8082}")
public interface BuildingClient {

    @GetMapping(path = "/api/building/{id}", produces = "application/json", consumes = "application/json")
    ResponseEntity<BuildingDto> getBuildingById(@PathVariable Long id);

}
