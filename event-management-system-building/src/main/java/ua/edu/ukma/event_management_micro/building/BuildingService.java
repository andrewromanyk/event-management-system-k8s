package ua.edu.ukma.event_management_micro.building;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ua.edu.ukma.event_management_micro.core.dto.LogEvent;

import java.util.List;
import java.util.Optional;

@Component
public class BuildingService {

    private ModelMapper modelMapper;
    private BuildingRepository buildingRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Autowired
    void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    void setUserRepository(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public BuildingDto createBuilding(BuildingDto building) {
        BuildingDto build = toDomain(buildingRepository.save(dtoToEntity(building)));
        applicationEventPublisher.publishEvent(new LogEvent(this, "Building created: " + build.getId()));
        return build;
    }

    public List<BuildingDto> getAllBuildings() {
        return buildingRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<BuildingDto> getBuildingById(Long id) {
        return Optional.ofNullable(buildingRepository
                .findById(id)
                .orElse(null)).map(this::toDomain);
    }

    public void updateBuilding(Long id, BuildingDto updatedBuilding) {
        Optional<BuildingEntity> existingBuilding = buildingRepository.findById(id);
        if (existingBuilding.isPresent()) {
            BuildingEntity building = existingBuilding.get();
            building.setAddress(updatedBuilding.getAddress());
            building.setHourlyRate(updatedBuilding.getHourlyRate());
            building.setAreaM2(updatedBuilding.getAreaM2());
            building.setCapacity(updatedBuilding.getCapacity());
            building.setDescription(updatedBuilding.getDescription());
            buildingRepository.save(building);
        }
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    public List<BuildingDto> getAllByAddressContaining(String address) {
        return buildingRepository.findAllByAddressContaining(address)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<BuildingDto> getAllByCapacity(int capacity) {
        return buildingRepository.findAllByCapacity(capacity)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public boolean buildingExists(Long buildingId) {
        return buildingRepository.existsById(buildingId);
//                .orElseThrow(() -> new NoSuchElementException("Building not found: " + buildingId));
//              .orElseGet(() -> buildingRepository.save(building));
    }

    private BuildingDto toDomain(BuildingEntity building) {
        return modelMapper.map(building, BuildingDto.class);
    }

    private BuildingEntity dtoToEntity(BuildingDto buildingDto) {
        return modelMapper.map(buildingDto, BuildingEntity.class);
    }
}
