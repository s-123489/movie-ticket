package com.zjsu.syt.cinema.payment.client;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TicketClientFallback implements TicketClient {

    @Override
    public Map<String, Object> confirmPayment(String id, String paymentId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Ticket service unavailable");
        return response;
    }

    @Override
    public Map<String, Object> cancelTicket(String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Ticket service unavailable");
        return response;
    }
}
