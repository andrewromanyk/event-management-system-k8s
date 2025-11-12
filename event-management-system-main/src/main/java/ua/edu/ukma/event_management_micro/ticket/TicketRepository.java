package ua.edu.ukma.event_management_micro.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findAllByOwner(long userId);
    Integer countAllByEvent(Long event);
    List<TicketEntity> findAllByEventIn(List<Long> events);
}

