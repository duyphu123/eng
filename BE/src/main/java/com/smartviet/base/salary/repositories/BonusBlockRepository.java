package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.BonusBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusBlockRepository extends JpaRepository<BonusBlock, Long> {
}