package com.example.payment_service_app.repository;

import com.example.payment_service_app.entity.OutboxMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage , Long> {
    List<OutboxMessage> findByProcessedFalseOrderByCreatedAtAsc(Pageable pageable);
}
