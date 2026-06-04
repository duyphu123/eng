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
@Table(name = "calc_make_payment_by_day_detail")
public class CalcMakePaymentByDayDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "calc_make_payment_by_day_id", nullable = false)
    private Long calcMakePaymentByDayId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "report_day")
    private Integer reportDay;

    @Column(name = "val_b")
    private Integer valB;

    @Column(name = "val_a")
    private Integer valA;

    @Column(name = "val_total")
    private Integer valTotal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

}
