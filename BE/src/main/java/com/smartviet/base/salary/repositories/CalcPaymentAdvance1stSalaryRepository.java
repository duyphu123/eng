package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcPaymentAdvance1stSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcPaymentAdvance1stSalaryRepository extends JpaRepository<CalcPaymentAdvance1stSalary, Long> {
}