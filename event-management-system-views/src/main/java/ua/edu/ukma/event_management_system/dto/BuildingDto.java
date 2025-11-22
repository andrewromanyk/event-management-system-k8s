package ua.edu.ukma.event_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDto {
    private Long id;
    @NotBlank(message = "Address is required")
    private String address;
    @Min(value = 1, message = "Hourly rate is required and must be positive")
    private int hourlyRate;
    @Min(value = 10, message = "Area in m2 should be at least 10")
    private int areaM2;
    @Min(value = 10, message = "Capacity should be at least 10")
    private int capacity;
    @Pattern(regexp = "^[A-Za-zА-Яа-я0-9 ,.:;-]+|^$", message = "Description contains invalid characters")
    @Size(max = 500, message = "Description must be less than 500 characters long")
    private String description;

}