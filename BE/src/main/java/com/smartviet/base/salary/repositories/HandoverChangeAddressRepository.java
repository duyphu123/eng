package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.HandoverChangeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverChangeAddressRepository extends JpaRepository<HandoverChangeAddress, Long> {
}