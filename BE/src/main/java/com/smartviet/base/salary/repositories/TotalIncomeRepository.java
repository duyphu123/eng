package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.TotalIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalIncomeRepository extends JpaRepository<TotalIncome, Long> {
}