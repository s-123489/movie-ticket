package com.zjsu.syt.cinema.ticket.repository;

import com.zjsu.syt.cinema.ticket.model.Ticket;
import com.zjsu.syt.cinema.ticket.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByUserId(String userId);
    List<Ticket> findByShowtimeId(String showtimeId);
    List<Ticket> findByShowtimeIdAndStatus(String showtimeId, TicketStatus status);
    List<Ticket> findByUserIdOrderByCreatedAtDesc(String userId);
    boolean existsByShowtimeIdAndSeatNumberAndStatusIn(String showtimeId, String seatNumber, List<TicketStatus> statuses);
}
