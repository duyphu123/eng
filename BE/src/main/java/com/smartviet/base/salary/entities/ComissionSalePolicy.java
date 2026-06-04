package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comission_sale_policy")
public class ComissionSalePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "service_type", nullable = false, length = 100)
    private String serviceType;

    @Column(name = "package_fee_from", nullable = false)
    private BigDecimal packageFeeFrom;

    @Column(name = "package_fee_to")
    private BigDecimal packageFeeTo;

    @Column(name = "months_adv_from", nullable = false)
    private Integer monthsAdvFrom;

    @Column(name = "months_adv_to")
    private Integer monthsAdvTo;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "is_percent", nullable = false)
    private Boolean isPercent;

    @Column(name = "commission", nullable = false)
    private BigDecimal commission;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

}
