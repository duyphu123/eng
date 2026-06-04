package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcPayAdv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcPayAdvRepository extends JpaRepository<CalcPayAdv, Long> {
}