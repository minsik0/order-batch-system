package com.sparta.orderbatchsystem.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlement",
uniqueConstraints = @UniqueConstraint(columnNames = {"settlement_date", "order_id"})) // Idempotency 핵심
@Getter
@NoArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate settlementDate;
    private Long orderId;
    private Long sellerId;
    private BigDecimal orderAmount;
    private BigDecimal feeAmount;
    private BigDecimal netAmount;       // 셀러 실수령액

    @Enumerated(EnumType.STRING)
    private SettlementStatus status;    // PENDING, COMPLETED

    private LocalDateTime processedAt;
}
