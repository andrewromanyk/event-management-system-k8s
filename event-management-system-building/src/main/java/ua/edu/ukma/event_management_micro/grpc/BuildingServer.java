package ua.edu.ukma.event_management_micro.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_micro.building.BuildingEntity;
import ua.edu.ukma.event_management_micro.building.BuildingRepository;
import ua.edu.ukma.event_management_system_building.grpc.Building;
import ua.edu.ukma.event_management_system_building.grpc.BuildingID;
import ua.edu.ukma.event_management_system_building.grpc.BuildingServiceGrpc;

import java.util.List;


@Service
public class BuildingServer extends BuildingServiceGrpc.BuildingServiceImplBase {
    private final BuildingRepository repo;

    @Autowired
    BuildingServer(BuildingRepository repo) {
        this.repo = repo;
    }

    @Override
    public void getBuildingById(BuildingID reqId, StreamObserver<Building> resp) {
        long id = reqId.getId();
        BuildingEntity building = repo.findById(id).orElse(null);

        if (building == null) {
            resp.onError(new RuntimeException("Building with id " + id + " not found"));
            return;
        }

        resp.onNext(Building.newBuilder()
                .setId(id)
                .setAddress(building.getAddress())
                .setHourlyRate(building.getHourlyRate())
                .setAreaM2(building.getAreaM2())
                .setCapacity(building.getCapacity())
                .setDescription(building.getDescription() == null ? "" : building.getDescription())
                .build());
        resp.onCompleted();
    }

    @Override
    public void streamAllBuildings(Empty request, StreamObserver<Building> resp) {
        List<BuildingEntity> buildings = repo.findAll();

        if (buildings.isEmpty()) {
            resp.onError(new RuntimeException("No buildings found"));
            return;
        }

        for (BuildingEntity building : buildings) {
            resp.onNext(Building.newBuilder()
                    .setId(building.getId())
                    .setAddress(building.getAddress())
                    .setHourlyRate(building.getHourlyRate())
                    .setAreaM2(building.getAreaM2())
                    .setCapacity(building.getCapacity())
                    .setDescription(building.getDescription() == null ? "" : building.getDescription())
                    .build());
        }
        resp.onCompleted();
    }
}
