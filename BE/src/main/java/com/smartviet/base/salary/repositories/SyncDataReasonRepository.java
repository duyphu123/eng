package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncDataReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncDataReasonRepository extends JpaRepository<SyncDataReason, Long> {
}