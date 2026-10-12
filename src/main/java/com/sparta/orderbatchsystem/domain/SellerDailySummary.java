package com.sparta.orderbatchsystem.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "seller_daily_summary")
@Getter
@NoArgsConstructor
public class SellerDailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate summaryDate;
    private Long sellerId;
    private Integer totalOrderCount;
    private BigDecimal totalOrderAmount;
    private BigDecimal totalFeeAmount;
    private BigDecimal totalNetAmount;
}
