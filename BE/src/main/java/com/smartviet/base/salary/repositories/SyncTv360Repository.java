package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncTv360;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncTv360Repository extends JpaRepository<SyncTv360, Long> {
}