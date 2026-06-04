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
@Table(name = "calc_hotline_return")
public class CalcHotlineReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "penalty_type", nullable = false, length = 50)
    private String penaltyType;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "date_register")
    private LocalDate dateRegister;

    @Column(name = "deduct_commission")
    private BigDecimal deductCommission;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
