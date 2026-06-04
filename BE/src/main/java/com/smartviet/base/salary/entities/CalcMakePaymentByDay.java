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
@Table(name = "calc_make_payment_by_day")
public class CalcMakePaymentByDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "staff_name", length = 150)
    private String staffName;

    @Column(name = "staff_user", length = 100)
    private String staffUser;

    @Column(name = "remark", length = 255)
    private String remark;

    @Column(name = "follow_day_lt_300_b")
    private Integer followDayLt300B;

    @Column(name = "follow_day_lt_300_a")
    private Integer followDayLt300A;

    @Column(name = "follow_day_lt_300_total")
    private Integer followDayLt300Total;

    @Column(name = "to_299_sub")
    private Integer to299Sub;

    @Column(name = "follow_day_300_up_b")
    private Integer followDay300UpB;

    @Column(name = "follow_day_300_up_a")
    private Integer followDay300UpA;

    @Column(name = "follow_day_300_up_total")
    private Integer followDay300UpTotal;

    @Column(name = "add_sub_lt_300_b")
    private Integer addSubLt300B;

    @Column(name = "add_sub_lt_300_a")
    private Integer addSubLt300A;

    @Column(name = "add_sub_lt_300_total")
    private Integer addSubLt300Total;

    @Column(name = "add_sub_300_up_b")
    private Integer addSub300UpB;

    @Column(name = "add_sub_300_up_a")
    private Integer addSub300UpA;

    @Column(name = "add_sub_300_up_total")
    private Integer addSub300UpTotal;

    @Column(name = "pre_n_plus_1")
    private Integer preNPlus1;

    @Column(name = "final_total")
    private Integer finalTotal;

    @Column(name = "check_status", length = 50)
    private String checkStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

}
