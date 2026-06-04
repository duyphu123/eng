package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.PaymentCorrect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCorrectRepository extends JpaRepository<PaymentCorrect, Long> {
}