package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncRecoveryRepository extends JpaRepository<SyncRecovery, Long> {
}