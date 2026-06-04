package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcChangeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcChangeAddressRepository extends JpaRepository<CalcChangeAddress, Long> {
}