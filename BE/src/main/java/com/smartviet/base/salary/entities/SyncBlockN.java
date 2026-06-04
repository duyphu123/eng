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
@Table(name = "sync_block_n")
public class SyncBlockN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "shop_code", length = 50)
    private String shopCode;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "sub_type", length = 50)
    private String subType;

    @Column(name = "bus_type", length = 50)
    private String busType;

    @Column(name = "package_name", length = 100)
    private String packageName;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    @Column(name = "first_connect")
    private LocalDate firstConnect;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "prom_code", length = 50)
    private String promCode;

    @Column(name = "reg_reason", length = 255)
    private String regReason;

    @Column(name = "finished_reason", length = 255)
    private String finishedReason;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "dslam_name", length = 100)
    private String dslamName;

    @Column(name = "restore_date")
    private LocalDate restoreDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "dev_code_sale", length = 50)
    private String devCodeSale;

    @Column(name = "channel", length = 100)
    private String channel;

    @Column(name = "service_type", length = 100)
    private String serviceType;

    @Column(name = "product_code", length = 100)
    private String productCode;

    @Column(name = "barring_date")
    private LocalDate barringDate;

    @Column(name = "reason_restore", length = 255)
    private String reasonRestore;

    @Column(name = "remain_prepaid")
    private BigDecimal remainPrepaid;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
