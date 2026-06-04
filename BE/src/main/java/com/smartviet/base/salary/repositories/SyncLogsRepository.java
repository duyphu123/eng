package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncLogsRepository extends JpaRepository<SyncLogs, Long> {
}