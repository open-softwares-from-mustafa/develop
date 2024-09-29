package com.brokerage.model;

import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Data
@Table(name = "ORDERS")
@RequiredArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String assetName;

    @Enumerated(EnumType.STRING)
    private Side orderSide;

    private int size;
    private double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createDate;

    public Order(Long customerId, String assetName, Side orderSide, int size, double price, Status status, LocalDateTime dateTime) {
        this.customerId = customerId;
        this.assetName = assetName;
        this.orderSide = orderSide;
        this.size = size;
        this.price = price;
        this.status = status;
        this.createDate = dateTime;
    }
}
