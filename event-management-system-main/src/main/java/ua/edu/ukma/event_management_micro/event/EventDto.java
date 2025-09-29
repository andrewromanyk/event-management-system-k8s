package ua.edu.ukma.event_management_micro.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class EventDto {
    private Long id;
    private String eventTitle;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Long buildingId;
    private String description;
    private Integer numberOfTickets;
    private Integer minAgeRestriction;
//    private List<UserDto> users;
//    private byte[] image;
    private Long creatorId;
    private Double price;
}