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
@Table(name = "salary_commission_summary")
public class SalaryCommissionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "salary_type", nullable = false, length = 20)
    private String salaryType;

    @Column(name = "contract_type", length = 50)
    private String contractType;

    @Column(name = "staff_code", nullable = false, length = 50)
    private String staffCode;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "user_code_register", length = 255)
    private String userCodeRegister;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "working_date_dd")
    private Integer workingDateDd;

    @Column(name = "working_date_mm")
    private Integer workingDateMm;

    @Column(name = "working_date_yyyy")
    private Integer workingDateYyyy;

    @Column(name = "working_start_date")
    private LocalDate workingStartDate;

    @Column(name = "month_of_working")
    private Integer monthOfWorking;

    @Column(name = "real_working_day")
    private Integer realWorkingDay;

    @Column(name = "standard_working_day")
    private Integer standardWorkingDay;

    @Column(name = "total_new_contract_under_3m")
    private Integer totalNewContractUnder3m;

    @Column(name = "total_new_contract_3m_up")
    private Integer totalNewContract3mUp;

    @Column(name = "total_new_contract")
    private Integer totalNewContract;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "basic_salary")
    private BigDecimal basicSalary;

    @Column(name = "level_salary")
    private BigDecimal levelSalary;

    @Column(name = "bonus_block")
    private BigDecimal bonusBlock;

    @Column(name = "commission_sale")
    private BigDecimal commissionSale;

    @Column(name = "commission_change_address")
    private BigDecimal commissionChangeAddress;

    @Column(name = "commission_change_package")
    private BigDecimal commissionChangePackage;

    @Column(name = "commission_tv360")
    private BigDecimal commissionTv360;

    @Column(name = "commission_payment")
    private BigDecimal commissionPayment;

    @Column(name = "commission_recover")
    private BigDecimal commissionRecover;

    @Column(name = "bonus_new_staff_4_6_months")
    private BigDecimal bonusNewStaff46Months;

    @Column(name = "bonus_block_control_dealer")
    private BigDecimal bonusBlockControlDealer;

    @Column(name = "commission_gis")
    private BigDecimal commissionGis;

    @Column(name = "extra_amount")
    private BigDecimal extraAmount;

    @Column(name = "total_income_detail")
    private BigDecimal totalIncomeDetail;

    @Column(name = "total_income")
    private BigDecimal totalIncome;

    @Column(name = "support_gasoline")
    private BigDecimal supportGasoline;

    @Column(name = "penalty_return")
    private BigDecimal penaltyReturn;

    @Column(name = "income_before_tax")
    private BigDecimal incomeBeforeTax;

    @Column(name = "tax_wife")
    private Integer taxWife;

    @Column(name = "tax_child")
    private Integer taxChild;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "income_after_tax")
    private BigDecimal incomeAfterTax;

    @Column(name = "deduct_advance_1st_salary")
    private BigDecimal deductAdvance1stSalary;

    @Column(name = "actual_received")
    private BigDecimal actualReceived;

    @Column(name = "emoney_service_fee")
    private BigDecimal emoneyServiceFee;

    @Column(name = "emoney_account_number", length = 50)
    private String emoneyAccountNumber;

    @Column(name = "pay_cash_to_branch")
    private BigDecimal payCashToBranch;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

}
