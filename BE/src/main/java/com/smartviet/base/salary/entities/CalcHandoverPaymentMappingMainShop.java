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
@Table(name = "calc_handover_payment_mapping_main_shop")
public class CalcHandoverPaymentMappingMainShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "handover_payment_id", nullable = false)
    private Long handoverPaymentId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "payment_type", length = 100)
    private String paymentType;

    @Column(name = "total_paid")
    private BigDecimal totalPaid;

    @Column(name = "remain")
    private BigDecimal remain;

    @Column(name = "net_paid")
    private BigDecimal netPaid;

    @Column(name = "correct_paid")
    private BigDecimal correctPaid;

    @Column(name = "commission_debt_1d")
    private BigDecimal commissionDebt1d;

    @Column(name = "commission_debt_2d")
    private BigDecimal commissionDebt2d;

    @Column(name = "commission_debt_3d")
    private BigDecimal commissionDebt3d;

    @Column(name = "commission_debt_4d_up")
    private BigDecimal commissionDebt4dUp;

    @Column(name = "commission_code", length = 100)
    private String commissionCode;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "remark_b_a", length = 10)
    private String remarkBA;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "payment_type_remain", length = 150)
    private String paymentTypeRemain;

    @Column(name = "status", length = 100)
    private String status;

    @Column(name = "normal_debit", length = 100)
    private String normalDebit;

}
