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
@Table(name = "handover_payment")
public class HandoverPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "debit_type", length = 50)
    private String debitType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "service_type", length = 100)
    private String serviceType;

    @Column(name = "product_code", length = 100)
    private String productCode;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "unit_code", length = 50)
    private String unitCode;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "estimate_block")
    private LocalDate estimateBlock;

    @Column(name = "site_bts", length = 100)
    private String siteBts;

    @Column(name = "area_name", length = 255)
    private String areaName;

    @Column(name = "sr", length = 100)
    private String sr;

    @Column(name = "handover_staff_name", length = 255)
    private String handoverStaffName;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "code_clear_money", length = 50)
    private String codeClearMoney;

    @Column(name = "telephone", length = 50)
    private String telephone;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "status_handover", length = 50)
    private String statusHandover;

}
