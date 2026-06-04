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
@Table(name = "sync_detail_list")
public class SyncDetailList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "status_old_acc", length = 50)
    private String statusOldAcc;

    @Column(name = "old_account", length = 50)
    private String oldAccount;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "name_of_customer", length = 255)
    private String nameOfCustomer;

    @Column(name = "package_name", length = 100)
    private String packageName;

    @Column(name = "old_deposit")
    private BigDecimal oldDeposit;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "tel_fax", length = 50)
    private String telFax;

    @Column(name = "tel_service_name", length = 100)
    private String telServiceName;

    @Column(name = "sub_type", length = 50)
    private String subType;

    @Column(name = "first_connect")
    private LocalDate firstConnect;

    @Column(name = "bus_type", length = 150)
    private String busType;

    @Column(name = "shop_code", length = 50)
    private String shopCode;

    @Column(name = "user_created", length = 50)
    private String userCreated;

    @Column(name = "code_sale", length = 50)
    private String codeSale;

    @Column(name = "reason_reg", length = 255)
    private String reasonReg;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
