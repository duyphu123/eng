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
@Table(name = "calc_detail_list")
public class CalcDetailList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_id", nullable = false)
    private Long syncId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "day")
    private Integer day;

    @Column(name = "register_month")
    private Integer registerMonth;

    @Column(name = "register_year")
    private Integer registerYear;

    @Column(name = "main_shop")
    private BigDecimal mainShop;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "salary_process_type", length = 100)
    private String salaryProcessType;

    @Column(name = "pay_advance_new", length = 100)
    private String payAdvanceNew;

    @Column(name = "pay_advance_package", length = 100)
    private String payAdvancePackage;

    @Column(name = "commission_new")
    private BigDecimal commissionNew;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "register_date")
    private LocalDate registerDate;

}
