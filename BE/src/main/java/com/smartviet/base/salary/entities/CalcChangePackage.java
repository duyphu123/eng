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
@Table(name = "calc_change_package")
public class CalcChangePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_id", nullable = false)
    private Long syncId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "old_monthly_fee")
    private BigDecimal oldMonthlyFee;

    @Column(name = "new_monthly_fee")
    private BigDecimal newMonthlyFee;

    @Column(name = "payment")
    private BigDecimal payment;

    @Column(name = "month_adv")
    private Integer monthAdv;

    @Column(name = "user_pay", length = 50)
    private String userPay;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "staff_code", length = 100)
    private String staffCode;

    @Column(name = "staff_name", length = 255)
    private String staffName;

    @Column(name = "code_sale", length = 100)
    private String codeSale;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "branches", length = 100)
    private String branches;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
