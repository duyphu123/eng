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
@Table(name = "sync_location")
public class SyncLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "sub_id", length = 50)
    private String subId;

    @Column(name = "account", length = 50)
    private String account;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "deploy_area_code", length = 50)
    private String deployAreaCode;

    @Column(name = "service_type", length = 100)
    private String serviceType;

    @Column(name = "product_code", length = 100)
    private String productCode;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "cust_lat")
    private BigDecimal custLat;

    @Column(name = "cust_long")
    private BigDecimal custLong;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "finish_deploy_date")
    private LocalDate finishDeployDate;

    @Column(name = "updated_user", length = 50)
    private String updatedUser;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "trim_isdn", length = 50)
    private String trimIsdn;

    @Column(name = "day")
    private Integer day;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
