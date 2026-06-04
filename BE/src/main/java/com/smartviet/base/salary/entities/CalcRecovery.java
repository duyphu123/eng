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
@Table(name = "calc_recovery")
public class CalcRecovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_id", nullable = false)
    private Long syncId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "recover_no", length = 50)
    private String recoverNo;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "block_date")
    private LocalDate blockDate;

    @Column(name = "n_type", length = 50)
    private String nType;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "week", length = 20)
    private String week;

    @Column(name = "duplicate", length = 50)
    private String duplicate;

    @Column(name = "amount_in_system")
    private BigDecimal amountInSystem;

    @Column(name = "payment")
    private BigDecimal payment;

    @Column(name = "paid_monthly_fee", length = 50)
    private String paidMonthlyFee;

    @Column(name = "remark_amount_in_system", length = 500)
    private String remarkAmountInSystem;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
