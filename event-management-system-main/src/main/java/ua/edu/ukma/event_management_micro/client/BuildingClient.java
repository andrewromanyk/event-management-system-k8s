package ua.edu.ukma.event_management_micro.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.edu.ukma.event_management_micro.core.dto.BuildingDto;

@FeignClient(
        name = "event-management-building",
        url = "${building.service.url}"
//        configuration = "BuildingFeignConfig.class"
)
public interface BuildingClient {

    @GetMapping("/api/building/{id}")
    BuildingDto getBuildingById(@PathVariable Long id);
}