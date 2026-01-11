package com.zjsu.syt.cinema.user.repository;

import com.zjsu.syt.cinema.user.model.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, String> {
    List<PurchaseHistory> findByUserIdOrderByPurchasedAtDesc(String userId);
    List<PurchaseHistory> findByTicketId(String ticketId);
}
