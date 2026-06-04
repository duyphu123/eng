package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CommissionsTv360Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionsTv360PolicyRepository extends JpaRepository<CommissionsTv360Policy, Long> {
}