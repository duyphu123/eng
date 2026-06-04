package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.BonusVipConfigPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusVipConfigPolicyRepository extends JpaRepository<BonusVipConfigPolicy, Long> {
}