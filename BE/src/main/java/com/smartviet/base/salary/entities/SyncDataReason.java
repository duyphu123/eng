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
@Table(name = "sync_data_reason")
public class SyncDataReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "account", length = 50)
    private String account;

    @Column(name = "user_using", length = 100)
    private String userUsing;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "service_type", length = 100)
    private String serviceType;

    @Column(name = "product_code", length = 100)
    private String productCode;

    @Column(name = "sub_type", length = 50)
    private String subType;

    @Column(name = "first_connect")
    private LocalDate firstConnect;

    @Column(name = "change_datetime")
    private LocalDateTime changeDatetime;

    @Column(name = "tel_fax", length = 50)
    private String telFax;

    @Column(name = "tel_mobile", length = 50)
    private String telMobile;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "create_user", length = 50)
    private String createUser;

    @Column(name = "action_type_name", length = 100)
    private String actionTypeName;

    @Column(name = "reason_name", length = 255)
    private String reasonName;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
