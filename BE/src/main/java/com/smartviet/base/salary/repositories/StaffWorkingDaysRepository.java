package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.StaffWorkingDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffWorkingDaysRepository extends JpaRepository<StaffWorkingDays, Long> {
}