package ua.edu.ukma.event_management_micro.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReturnDto {
    private Long ticketId;
    private Long eventId;
    private Long userId;
    private String userEmail;
    private String reason;

}
