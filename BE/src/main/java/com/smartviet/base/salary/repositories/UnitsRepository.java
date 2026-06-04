package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitsRepository extends JpaRepository<Units, Long> {
}