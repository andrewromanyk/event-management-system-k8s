package ua.edu.ukma.event_management_micro.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReturnDto implements Serializable {
    private Long ticketId;
    private Long eventId;
    private Long userId;
    private String userEmail;
    private String reason;
}
