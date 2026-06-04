package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.EmployeeCodeSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCodeSalesRepository extends JpaRepository<EmployeeCodeSales, Long> {
}