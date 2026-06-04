package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.OptionSetValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionSetValueRepository extends JpaRepository<OptionSetValue, Long> {
}