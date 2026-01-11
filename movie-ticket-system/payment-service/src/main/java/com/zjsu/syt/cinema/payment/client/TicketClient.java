package com.zjsu.syt.cinema.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ticket-service", fallback = TicketClientFallback.class)
public interface TicketClient {

    @PostMapping("/api/tickets/{id}/confirm")
    Map<String, Object> confirmPayment(@PathVariable("id") String id, @RequestParam String paymentId);

    @PostMapping("/api/tickets/{id}/cancel")
    Map<String, Object> cancelTicket(@PathVariable("id") String id);
}
