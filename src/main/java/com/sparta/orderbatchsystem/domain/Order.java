package com.sparta.orderbatchsystem.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    private Long id;

    private Long userId;
    private Long productId;
    private String status;          // COMPLETED, CANCELLED, REFUNDED
    private BigDecimal amount;
    private BigDecimal feeRate;     // 수수료율 (예: 0.035 = 3.5%)
    private LocalDateTime orderedAt;
    private LocalDateTime completedAt;
}
