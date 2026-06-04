package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "personal_income_tax")
public class PersonalIncomeTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "salary_type", nullable = false, length = 50)
    private String salaryType;

    @Column(name = "total_salary_from", nullable = false)
    private BigDecimal totalSalaryFrom;

    @Column(name = "total_salary_to")
    private BigDecimal totalSalaryTo;

    @Column(name = "tax", nullable = false)
    private BigDecimal tax;

    @Column(name = "deduction_amount", nullable = false)
    private BigDecimal deductionAmount;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

}
