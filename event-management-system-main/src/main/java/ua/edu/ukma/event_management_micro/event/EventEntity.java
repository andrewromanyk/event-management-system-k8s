package ua.edu.ukma.event_management_micro.event;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String eventTitle;

    @Column(nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(nullable = false)
    private LocalDateTime dateTimeEnd;

    @Column
    private Long buildingId;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int numberOfTickets;

    @Column(nullable = false)
    private int minAgeRestriction;


    @Column
    private Long creatorId;

    @Column(nullable = false)
    private double price;


}