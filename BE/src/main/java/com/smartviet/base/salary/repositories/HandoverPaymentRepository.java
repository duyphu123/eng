package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.HandoverPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverPaymentRepository extends JpaRepository<HandoverPayment, Long> {
}