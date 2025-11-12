package ua.edu.ukma.event_management_micro.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_micro.core.dto.BuildingDto;

import java.util.List;

@FeignClient(name = "building-client", url = "${feign.client.building.url}")
public interface BuildingClient {

    @GetMapping("/api/building/{id}")
    ResponseEntity<BuildingDto> getBuildingById(@PathVariable Long id);

}
