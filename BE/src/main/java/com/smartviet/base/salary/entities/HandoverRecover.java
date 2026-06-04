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
@Table(name = "handover_recover")
public class HandoverRecover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "staff_code", length = 50)
    private String staffCode;

    @Column(name = "staff_name", length = 255)
    private String staffName;

    @Column(name = "user_payment", length = 50)
    private String userPayment;

    @Column(name = "emoney", length = 100)
    private String emoney;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
