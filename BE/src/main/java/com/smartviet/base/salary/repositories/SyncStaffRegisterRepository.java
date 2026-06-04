package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncStaffRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncStaffRegisterRepository extends JpaRepository<SyncStaffRegister, Long> {
}