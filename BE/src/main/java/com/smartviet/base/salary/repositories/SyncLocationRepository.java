package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncLocationRepository extends JpaRepository<SyncLocation, Long> {
}