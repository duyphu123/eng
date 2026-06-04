package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "calc_payment_advance_1st_salary")
public class CalcPaymentAdvance1stSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "staff_code", nullable = false, length = 50)
    private String staffCode;

    @Column(name = "total_contract")
    private Integer totalContract;

    @Column(name = "point_of_person")
    private BigDecimal pointOfPerson;

    @Column(name = "rank_of_person")
    private BigDecimal rankOfPerson;

    @Column(name = "pi")
    private BigDecimal pi;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "extra")
    private BigDecimal extra;

    @Column(name = "deduct")
    private BigDecimal deduct;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "httt")
    private BigDecimal httt;

    @Column(name = "position_salary")
    private BigDecimal positionSalary;

    @Column(name = "bussiness_salary")
    private BigDecimal bussinessSalary;

}
