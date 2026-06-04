package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.ComissionSalePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissionSalePolicyRepository extends JpaRepository<ComissionSalePolicy, Long> {
}