package com.zjsu.syt.cinema.ticket.controller;

import com.zjsu.syt.cinema.ticket.model.Ticket;
import com.zjsu.syt.cinema.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TicketController {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;

    @Value("${server.port}")
    private String currentPort;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    private String getHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null && !hostname.isEmpty()) {
            return hostname;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Failed to get hostname: {}", e.getMessage());
        }
        return "unknown-" + currentPort;
    }

    @PostMapping("/tickets/book")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> bookTicket(@Valid @RequestBody BookTicketRequest request) {
        log.info("Ticket Service [port: {}, hostname: {}] booking ticket for user: {}",
                currentPort, getHostname(), request.userId());

        try {
            Ticket ticket = ticketService.bookTicket(
                    request.userId(),
                    request.showtimeId(),
                    request.movieId(),
                    request.seatNumber(),
                    request.price()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("data", TicketResponse.from(ticket));
            response.put("status", "SUCCESS");
            return response;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("port", currentPort);
            errorResponse.put("hostname", getHostname());
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/tickets")
    public Map<String, Object> getAllTickets() {
        log.info("Ticket Service [port: {}, hostname: {}] getting all tickets",
                currentPort, getHostname());

        List<TicketResponse> tickets = ticketService.getAllTickets().stream()
                .map(TicketResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", tickets);
        response.put("count", tickets.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Map<String, Object>> getTicketById(@PathVariable String id) {
        log.info("Ticket Service [port: {}, hostname: {}] getting ticket by id: {}",
                currentPort, getHostname(), id);

        return ticketService.getTicketById(id)
                .map(ticket -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", TicketResponse.from(ticket));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "Ticket with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @GetMapping("/tickets/user/{userId}")
    public Map<String, Object> getTicketsByUser(@PathVariable String userId) {
        log.info("Ticket Service [port: {}, hostname: {}] getting tickets for user: {}",
                currentPort, getHostname(), userId);

        List<TicketResponse> tickets = ticketService.getTicketsByUser(userId).stream()
                .map(TicketResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", tickets);
        response.put("count", tickets.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/tickets/showtime/{showtimeId}")
    public Map<String, Object> getTicketsByShowtime(@PathVariable String showtimeId) {
        log.info("Ticket Service [port: {}, hostname: {}] getting tickets for showtime: {}",
                currentPort, getHostname(), showtimeId);

        List<TicketResponse> tickets = ticketService.getTicketsByShowtime(showtimeId).stream()
                .map(TicketResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", tickets);
        response.put("count", tickets.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @PostMapping("/tickets/{id}/confirm")
    public ResponseEntity<Map<String, Object>> confirmPayment(
            @PathVariable String id,
            @RequestParam String paymentId) {
        log.info("Ticket Service [port: {}, hostname: {}] confirming payment for ticket: {}",
                currentPort, getHostname(), id);

        try {
            Ticket ticket = ticketService.confirmPayment(id, paymentId);
            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("data", TicketResponse.from(ticket));
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("port", currentPort);
            errorResponse.put("hostname", getHostname());
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/tickets/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelTicket(@PathVariable String id) {
        log.info("Ticket Service [port: {}, hostname: {}] cancelling ticket: {}",
                currentPort, getHostname(), id);

        try {
            ticketService.cancelTicket(id);
            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("status", "SUCCESS");
            response.put("message", "Ticket cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("port", currentPort);
            errorResponse.put("hostname", getHostname());
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("port", currentPort);
        health.put("hostname", getHostname());
        health.put("service", "ticket-service");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }

    record BookTicketRequest(
            String userId,
            String showtimeId,
            String movieId,
            String seatNumber,
            Double price
    ) {}

    record TicketResponse(
            String id,
            String userId,
            String showtimeId,
            String movieId,
            String seatNumber,
            Double price,
            String status,
            String paymentId,
            String bookingTime,
            String expiryTime,
            String createdAt
    ) {
        static TicketResponse from(Ticket ticket) {
            return new TicketResponse(
                    ticket.getId(),
                    ticket.getUserId(),
                    ticket.getShowtimeId(),
                    ticket.getMovieId(),
                    ticket.getSeatNumber(),
                    ticket.getPrice(),
                    ticket.getStatus() != null ? ticket.getStatus().name() : null,
                    ticket.getPaymentId(),
                    ticket.getBookingTime() != null ? ticket.getBookingTime().toString() : null,
                    ticket.getExpiryTime() != null ? ticket.getExpiryTime().toString() : null,
                    ticket.getCreatedAt() != null ? ticket.getCreatedAt().toString() : null
            );
        }
    }
}
