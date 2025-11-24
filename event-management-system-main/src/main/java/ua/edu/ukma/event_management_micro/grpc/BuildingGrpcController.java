package ua.edu.ukma.event_management_micro.grpc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.event_management_micro.core.error.GrpcError;
import ua.edu.ukma.event_management_system_building.grpc.Building;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/grpc/building")
public class BuildingGrpcController {

    private final BuildingGrpcClient client;

    public BuildingGrpcController(BuildingGrpcClient client) {
        this.client = client;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getBuilding(@PathVariable long id) throws InvalidProtocolBufferException {
        Building building = client.getBuildingById(id);

        String json = JsonFormat.printer()
                .print(building);

        return ResponseEntity.ok(json);
    }

    @GetMapping("/all-buildings")
    public ResponseEntity<String> streamAllBuildings() throws InvalidProtocolBufferException {
        List<Building> buildings;
        try {
            buildings = client.streamAllBuildings();
        }
        catch (Exception e) {
            throw new GrpcError(e.getMessage());
        }
        List<String> jsonItems = new ArrayList<>();

        for (Building b : buildings) {
            String json = JsonFormat.printer()
                    .print(b);
            jsonItems.add(json);
        }

        String jsonArray = "[" + String.join(",", jsonItems) + "]";

        return ResponseEntity.ok(jsonArray);
    }
}