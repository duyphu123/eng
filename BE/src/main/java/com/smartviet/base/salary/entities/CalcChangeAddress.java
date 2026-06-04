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
@Table(name = "calc_change_address")
public class CalcChangeAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "handover_id", nullable = false)
    private Long handoverId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "code_sale", length = 50)
    private String codeSale;

    @Column(name = "staff_name", length = 255)
    private String staffName;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "pay_advance")
    private BigDecimal payAdvance;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
