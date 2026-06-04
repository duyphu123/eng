package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.HandoverChangePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverChangePackageRepository extends JpaRepository<HandoverChangePackage, Long> {
}