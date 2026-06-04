package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcBlockN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcBlockNRepository extends JpaRepository<CalcBlockN, Long> {
}