package ua.edu.ukma.event_management_micro.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findAllByOwner(long userId);
    Integer countAllByEvent(Long event);
//    List<TicketEntity> findAllByUserUsername(String username);
//    @Query("SELECT t.id FROM TicketEntity t WHERE DATEDIFF(DAY, now(), t.purchaseDate) <= 1")
//    List<Long> findAllCreatedToday();
//    List<TicketEntity> findTicketEntitiesByEvent_Creator_Id(long creator);
}

