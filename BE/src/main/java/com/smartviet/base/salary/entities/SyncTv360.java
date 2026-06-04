package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sync_tv360")
public class SyncTv360 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "contract_id", length = 50)
    private String contractId;

    @Column(name = "sub_id", length = 50)
    private String subId;

    @Column(name = "sale_trans_id", length = 50)
    private String saleTransId;

    @Column(name = "first_connect")
    private LocalDate firstConnect;

    @Column(name = "deploy_address", length = 500)
    private String deployAddress;

    @Column(name = "account", length = 50)
    private String account;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "team_id", length = 50)
    private String teamId;

    @Column(name = "staff_id", length = 50)
    private String staffId;

    @Column(name = "user_created", length = 50)
    private String userCreated;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "sta_datetime")
    private LocalDateTime staDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "cust_name", length = 255)
    private String custName;

    @Column(name = "staff_phone", length = 50)
    private String staffPhone;

    @Column(name = "staff_status", length = 50)
    private String staffStatus;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "act_status", length = 50)
    private String actStatus;

    @Column(name = "product_code", length = 100)
    private String productCode;

    @Column(name = "isdn_ftth", length = 50)
    private String isdnFtth;

    @Column(name = "code_sale", length = 50)
    private String codeSale;

    @Column(name = "reason_reg", length = 255)
    private String reasonReg;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
