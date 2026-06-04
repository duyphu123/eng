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
@Table(name = "sync_change_address")
public class SyncChangeAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "name_customer", length = 100)
    private String nameCustomer;

    @Column(name = "package", length = 100)
    private String packageField;

    @Column(name = "tel_fax", length = 50)
    private String telFax;

    @Column(name = "sub_type", length = 50)
    private String subType;

    @Column(name = "first_connect")
    private LocalDate firstConnect;

    @Column(name = "block_status", length = 50)
    private String blockStatus;

    @Column(name = "reason_change_address", length = 255)
    private String reasonChangeAddress;

    @Column(name = "user_change", length = 50)
    private String userChange;

    @Column(name = "date_change")
    private LocalDate dateChange;

    @Column(name = "bus_type", length = 50)
    private String busType;

    @Column(name = "money_remain")
    private BigDecimal moneyRemain;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
