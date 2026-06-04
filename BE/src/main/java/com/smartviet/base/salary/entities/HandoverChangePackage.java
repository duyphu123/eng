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
@Table(name = "handover_change_package")
public class HandoverChangePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "account", length = 50)
    private String account;

    @Column(name = "old_package", length = 100)
    private String oldPackage;

    @Column(name = "old_package_fee")
    private BigDecimal oldPackageFee;

    @Column(name = "new_package", length = 100)
    private String newPackage;

    @Column(name = "new_package_fee")
    private BigDecimal newPackageFee;

    @Column(name = "payment_opption", length = 100)
    private String paymentOpption;

    @Column(name = "staff_upgrade", length = 255)
    private String staffUpgrade;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "emoney_account", length = 50)
    private String emoneyAccount;

    @Column(name = "user_to_change_package", length = 100)
    private String userToChangePackage;

    @Column(name = "department", length = 50)
    private String department;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
