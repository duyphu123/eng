package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.BonusNewStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusNewStaffRepository extends JpaRepository<BonusNewStaff, Long> {
}