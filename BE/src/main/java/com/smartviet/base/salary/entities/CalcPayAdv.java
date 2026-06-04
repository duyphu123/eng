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
@Table(name = "calc_pay_adv")
public class CalcPayAdv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "service", length = 100)
    private String service;

    @Column(name = "handover_id")
    private Long handoverId;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "payment_code", length = 100)
    private String paymentCode;

    @Column(name = "calculated_code", length = 100)
    private String calculatedCode;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "pay_adv_amount")
    private BigDecimal payAdvAmount;

    @Column(name = "month_pay_advance")
    private Integer monthPayAdvance;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
