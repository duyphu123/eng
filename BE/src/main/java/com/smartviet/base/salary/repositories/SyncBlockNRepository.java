package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncBlockN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncBlockNRepository extends JpaRepository<SyncBlockN, Long> {
}