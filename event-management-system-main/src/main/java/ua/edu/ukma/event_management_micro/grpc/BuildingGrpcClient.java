package ua.edu.ukma.event_management_micro.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_system_building.grpc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class BuildingGrpcClient {

    private final BuildingServiceGrpc.BuildingServiceBlockingStub blockingStub;
    private final BuildingServiceGrpc.BuildingServiceStub asyncStub;

    public BuildingGrpcClient(
            BuildingServiceGrpc.BuildingServiceBlockingStub blockingStub,
            BuildingServiceGrpc.BuildingServiceStub asyncStub
    ) {
        this.blockingStub = blockingStub;
        this.asyncStub = asyncStub;
    }

    public Building getBuildingById(long id) {
        BuildingID request = BuildingID.newBuilder()
                .setId(id)
                .build();

        return blockingStub.getBuildingById(request);
    }

    public List<Building> streamAllBuildings() {
        List<Building> result = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<Building> observer = new StreamObserver<>() {

            @Override
            public void onNext(Building building) {
                result.add(building);
            }

            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                latch.countDown();
            }

            public void onCompleted() {
                latch.countDown();
            }
        };

        asyncStub.streamAllBuildings(Empty.getDefaultInstance(), observer);

        try {
            latch.await();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        return result;
    }
}
