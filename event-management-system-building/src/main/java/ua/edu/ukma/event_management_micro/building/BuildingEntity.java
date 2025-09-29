package ua.edu.ukma.event_management_micro.building;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity(name = "building")
@ToString
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int hourlyRate;

    @Column(nullable = false)
    private int areaM2;

    @Column(nullable = false)
    private int capacity;

    @Column(length = 500)
    private String description;

    public BuildingEntity(String address, int hourlyRate, int areaM2, int capacity, String description) {
        this.address = address;
        this.hourlyRate = hourlyRate;
        this.areaM2 = areaM2;
        this.capacity = capacity;
        this.description = description;
    }
}
