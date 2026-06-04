package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncChangePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncChangePackageRepository extends JpaRepository<SyncChangePackage, Long> {
}