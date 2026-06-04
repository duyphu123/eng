package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.GasolineSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GasolineSupportRepository extends JpaRepository<GasolineSupport, Long> {
}