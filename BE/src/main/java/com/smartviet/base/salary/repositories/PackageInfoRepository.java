package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.PackageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageInfoRepository extends JpaRepository<PackageInfo, Long> {
}