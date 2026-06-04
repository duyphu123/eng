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
@Table(name = "sync_recovery")
public class SyncRecovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "shop_code", length = 50)
    private String shopCode;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "cust_id", length = 50)
    private String custId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "cust_type", length = 50)
    private String custType;

    @Column(name = "sub_type", length = 50)
    private String subType;

    @Column(name = "bus_type", length = 50)
    private String busType;

    @Column(name = "package_name", length = 100)
    private String packageName;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    @Column(name = "first_connect")
    private LocalDateTime firstConnect;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "prom_code", length = 50)
    private String promCode;

    @Column(name = "reg_reason", length = 255)
    private String regReason;

    @Column(name = "finished_reason", length = 255)
    private String finishedReason;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "dslam_name", length = 255)
    private String dslamName;

    @Column(name = "restore_date")
    private LocalDate restoreDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "code_sale", length = 50)
    private String codeSale;

    @Column(name = "channel", length = 100)
    private String channel;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "reason_restore", length = 255)
    private String reasonRestore;

    @Column(name = "barring_date")
    private LocalDate barringDate;

    @Column(name = "open_block_date")
    private LocalDate openBlockDate;

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "debit")
    private BigDecimal debit;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
