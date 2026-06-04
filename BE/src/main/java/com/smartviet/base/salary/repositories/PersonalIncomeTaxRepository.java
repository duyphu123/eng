package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.PersonalIncomeTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalIncomeTaxRepository extends JpaRepository<PersonalIncomeTax, Long> {
}