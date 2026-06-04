package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CommissionsPaymentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionsPaymentPolicyRepository extends JpaRepository<CommissionsPaymentPolicy, Long> {
}