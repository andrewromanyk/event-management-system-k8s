package ua.edu.ukma.event_management_micro.ticket;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private Long owner;

    @Column
    private Long event;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

}