package ua.edu.ukma.event_management_micro.building;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
    List<BuildingEntity> findAllByAddressContaining(String address);
    List<BuildingEntity> findAllByCapacity(int capacity);
}