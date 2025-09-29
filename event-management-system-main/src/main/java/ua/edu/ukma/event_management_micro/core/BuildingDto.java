package ua.edu.ukma.event_management_micro.core;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuildingDto {
    private int id;
    private String address;
    private int hourlyRate;
    private int areaM2;
    private int capacity;
    private String description;

    public BuildingDto(int id, String address, int hourlyRate, int areaM2, int capacity, String description) {
        this.id = id;
        this.address = address;
        this.hourlyRate = hourlyRate;
        this.areaM2 = areaM2;
        this.capacity = capacity;
        this.description = description;
    }

}