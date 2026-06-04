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
@Table(name = "bonus_new_staff")
public class BonusNewStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "salary_type", nullable = false, length = 50)
    private String salaryType;

    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "month_of_working_from", nullable = false)
    private Integer monthOfWorkingFrom;

    @Column(name = "month_of_working_to", nullable = false)
    private Integer monthOfWorkingTo;

    @Column(name = "number_contract_from", nullable = false)
    private Integer numberContractFrom;

    @Column(name = "number_contract_to")
    private Integer numberContractTo;

    @Column(name = "deduction_contract", nullable = false)
    private Integer deductionContract;

    @Column(name = "multiplier", nullable = false)
    private BigDecimal multiplier;

    @Column(name = "accumulated_money", nullable = false)
    private BigDecimal accumulatedMoney;

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
