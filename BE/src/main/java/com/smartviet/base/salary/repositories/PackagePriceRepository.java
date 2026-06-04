package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.PackagePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagePriceRepository extends JpaRepository<PackagePrice, Long> {
}