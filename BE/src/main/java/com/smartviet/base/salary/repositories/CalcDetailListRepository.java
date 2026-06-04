package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcDetailList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcDetailListRepository extends JpaRepository<CalcDetailList, Long> {
}