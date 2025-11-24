package ua.edu.ukma.event_management_micro.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_micro.core.error.GrpcError;
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

        try {
            return blockingStub.getBuildingById(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new GrpcError(e.getMessage());
            }
            throw e;
        }
    }

    public List<Building> streamAllBuildings() throws InterruptedException {
        List<Building> result = new ArrayList<>();
        final Throwable[] errorHandler = new Throwable[1];

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<Building> observer = new StreamObserver<>() {

            @Override
            public void onNext(Building building) {
                result.add(building);
            }

            public void onError(Throwable throwable) {
                errorHandler[0] = throwable;
                latch.countDown();
            }

            public void onCompleted() {
                latch.countDown();
            }
        };

        asyncStub.streamAllBuildings(Empty.getDefaultInstance(), observer);


        latch.await();

        if (errorHandler[0] != null) {
            StatusRuntimeException statusEx = (StatusRuntimeException) errorHandler[0];
            if (statusEx.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new GrpcError(statusEx.getMessage());
            }
        }

        return result;
    }
}
