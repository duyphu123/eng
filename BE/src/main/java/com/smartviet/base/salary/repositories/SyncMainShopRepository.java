package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.SyncMainShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncMainShopRepository extends JpaRepository<SyncMainShop, Long> {
}