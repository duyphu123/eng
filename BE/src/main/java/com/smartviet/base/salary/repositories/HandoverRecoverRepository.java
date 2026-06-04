package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.HandoverRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverRecoverRepository extends JpaRepository<HandoverRecover, Long> {
}