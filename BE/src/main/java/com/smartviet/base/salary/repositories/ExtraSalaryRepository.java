package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.ExtraSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraSalaryRepository extends JpaRepository<ExtraSalary, Long> {
}