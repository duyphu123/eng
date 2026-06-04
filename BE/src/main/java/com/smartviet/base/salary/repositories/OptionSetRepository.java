package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.OptionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionSetRepository extends JpaRepository<OptionSet, Long> {
}