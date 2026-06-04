package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncChangeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncChangeAddressRepository extends JpaRepository<SyncChangeAddress, Long> {
}