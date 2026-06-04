package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcHotlineReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcHotlineReturnRepository extends JpaRepository<CalcHotlineReturn, Long> {
}