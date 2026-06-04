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
@Table(name = "extra_salary")
public class ExtraSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "staff_code", nullable = false, length = 50)
    private String staffCode;

    @Column(name = "salary_type", nullable = false, length = 50)
    private String salaryType;

    @Column(name = "extra_amount", nullable = false)
    private BigDecimal extraAmount;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
