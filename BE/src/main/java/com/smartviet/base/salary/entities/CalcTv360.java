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
@Table(name = "calc_tv360")
public class CalcTv360 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_id", nullable = false)
    private Long syncId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "staff_code", length = 100)
    private String staffCode;

    @Column(name = "code_sale", length = 100)
    private String codeSale;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "pay_advance")
    private BigDecimal payAdvance;

    @Column(name = "rate_commission")
    private BigDecimal rateCommission;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
