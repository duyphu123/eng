package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.BasicSalaryConfigs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicSalaryConfigsRepository extends JpaRepository<BasicSalaryConfigs, Long> {
}