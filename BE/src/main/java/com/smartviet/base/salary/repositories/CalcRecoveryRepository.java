package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcRecoveryRepository extends JpaRepository<CalcRecovery, Long> {
}