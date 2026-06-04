package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.PaymentCompletionThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCompletionThresholdRepository extends JpaRepository<PaymentCompletionThreshold, Long> {
}