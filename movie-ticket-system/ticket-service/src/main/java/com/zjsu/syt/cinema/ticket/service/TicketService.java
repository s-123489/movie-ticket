package com.zjsu.syt.cinema.ticket.service;

import com.zjsu.syt.cinema.ticket.client.MovieClient;
import com.zjsu.syt.cinema.ticket.client.UserClient;
import com.zjsu.syt.cinema.ticket.model.Ticket;
import com.zjsu.syt.cinema.ticket.model.TicketStatus;
import com.zjsu.syt.cinema.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MovieClient movieClient;
    private final UserClient userClient;

    public TicketService(TicketRepository ticketRepository, MovieClient movieClient, UserClient userClient) {
        this.ticketRepository = ticketRepository;
        this.movieClient = movieClient;
        this.userClient = userClient;
    }

    @Transactional
    public Ticket bookTicket(String userId, String showtimeId, String movieId, String seatNumber, Double price) {
        Map<String, Object> userResponse = userClient.getUser(userId);
        if ("ERROR".equals(userResponse.get("status"))) {
            throw new RuntimeException("User not found or service unavailable");
        }

        Map<String, Object> showtimeResponse = movieClient.getShowtime(showtimeId);
        if ("ERROR".equals(showtimeResponse.get("status"))) {
            throw new RuntimeException("Showtime not found or service unavailable");
        }

        List<TicketStatus> occupiedStatuses = Arrays.asList(TicketStatus.RESERVED, TicketStatus.PAID);
        if (ticketRepository.existsByShowtimeIdAndSeatNumberAndStatusIn(showtimeId, seatNumber, occupiedStatuses)) {
            throw new RuntimeException("Seat already booked");
        }

        Map<String, Object> reserveResponse = movieClient.reserveSeats(showtimeId, 1);
        if ("ERROR".equals(reserveResponse.get("status"))) {
            throw new RuntimeException("Failed to reserve seat: " + reserveResponse.get("message"));
        }

        Ticket ticket = new Ticket(userId, showtimeId, movieId, seatNumber, price);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(String id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByUser(String userId) {
        return ticketRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Ticket> getTicketsByShowtime(String showtimeId) {
        return ticketRepository.findByShowtimeId(showtimeId);
    }

    @Transactional
    public Ticket confirmPayment(String ticketId, String paymentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.RESERVED) {
            throw new RuntimeException("Ticket is not in RESERVED status");
        }

        ticket.setStatus(TicketStatus.PAID);
        ticket.setPaymentId(paymentId);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelTicket(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new RuntimeException("Ticket already cancelled");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);

        movieClient.releaseSeats(ticket.getShowtimeId(), 1);
    }

    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(String id) {
        ticketRepository.deleteById(id);
    }
}
