package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcTv360;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcTv360Repository extends JpaRepository<CalcTv360, Long> {
}