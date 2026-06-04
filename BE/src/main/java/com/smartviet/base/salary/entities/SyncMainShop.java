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
@Table(name = "sync_main_shop")
public class SyncMainShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Column(name = "contract_no", length = 50)
    private String contractNo;

    @Column(name = "subscriber", length = 50)
    private String subscriber;

    @Column(name = "customer_name", length = 150)
    private String customerName;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "amount_not_tax")
    private BigDecimal amountNotTax;

    @Column(name = "amount_with_tax")
    private BigDecimal amountWithTax;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "amount_tax")
    private BigDecimal amountTax;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

    @Column(name = "withdraw_deposit_for_paid")
    private BigDecimal withdrawDepositForPaid;

    @Column(name = "after_deduct_deposit_amount")
    private BigDecimal afterDeductDepositAmount;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "invoice_no", length = 50)
    private String invoiceNo;

    @Column(name = "collection_code", length = 50)
    private String collectionCode;

    @Column(name = "payment_staff", length = 150)
    private String paymentStaff;

    @Column(name = "subscriber_type", length = 50)
    private String subscriberType;

    @Column(name = "service", length = 50)
    private String service;

    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Column(name = "sub_status", length = 50)
    private String subStatus;

    @Column(name = "sub_activate", length = 50)
    private String subActivate;

    @Column(name = "bus_type", length = 50)
    private String busType;

    @Column(name = "customer_type", length = 50)
    private String customerType;

    @Column(name = "debit_status", length = 50)
    private String debitStatus;

    @Column(name = "invoice_status", length = 50)
    private String invoiceStatus;

    @Column(name = "deadline_payment", length = 50)
    private String deadlinePayment;

    @Column(name = "transaction_id", length = 50)
    private String transactionId;

    @Column(name = "reason", length = 50)
    private String reason;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
