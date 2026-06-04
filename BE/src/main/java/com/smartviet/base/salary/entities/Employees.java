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
@Table(name = "employees")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "staff_code", nullable = false, length = 50)
    private String staffCode;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "position", length = 500)
    private String position;

    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "salary_process_type", length = 50)
    private String salaryProcessType;

    @Column(name = "contract_type")
    private Integer contractType;

    @Column(name = "working_start_date")
    private LocalDate workingStartDate;

    @Column(name = "working_end_date")
    private LocalDate workingEndDate;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "is_dealer_sale_point", nullable = false)
    private Boolean isDealerSalePoint;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "emoney", length = 100)
    private String emoney;

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
