package com.laptop.shopping.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TRANSACTION_ENTITY")

public class TransactionEntity extends AuditEntity {

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "TRANSACTION_CODE")
    private String transactionCode;

    @Column(name = "TRANSACTION_BILL")
    private String transactionBill;

    @Column(name = "SERVICE_PACKAGE_CODE")
    private String servicePackageCode;

    @Column(name = "ORGANIZATION_CODE")
    private String orgCode;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "AMOUNT")
    private String amount;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;

    @Column(name = "SECURE_HASH")
    private String secureHash;

    @Column(name = "VNP_TRANSACTION_STATUS")
    private String vnpTransactionStatus;

    @Column(name = "VNP_RESPONSE_CODE")
    private String vnpResponseCode;

}

