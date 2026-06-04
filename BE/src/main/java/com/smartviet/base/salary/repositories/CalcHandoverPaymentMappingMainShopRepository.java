package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.CalcHandoverPaymentMappingMainShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcHandoverPaymentMappingMainShopRepository extends JpaRepository<CalcHandoverPaymentMappingMainShop, Long> {
}