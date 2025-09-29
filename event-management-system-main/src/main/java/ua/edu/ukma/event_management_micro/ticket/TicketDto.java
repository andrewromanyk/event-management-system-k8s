package ua.edu.ukma.event_management_micro.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private Long id;
    private Long owner;
    private Long event;
    private Double price;
    private LocalDateTime purchaseDate;

}
