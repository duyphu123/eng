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
@Table(name = "payment_correct")
public class PaymentCorrect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "reason", length = 100)
    private String reason;

    @Column(name = "remark", length = 50)
    private String remark;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "remain_amount")
    private BigDecimal remainAmount;

    @Column(name = "correct_amount")
    private BigDecimal correctAmount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "old_price")
    private BigDecimal oldPrice;

    @Column(name = "new_price")
    private BigDecimal newPrice;

}
