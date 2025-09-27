package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buyerId", nullable = false)
    private User buyer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerId", nullable = false)
    private User seller;

    @Column(name = "orderDate")
    private Instant orderDate;

    @Size(max = 45)
    @Column(name = "shippingAddress", length = 45)
    private String shippingAddress;

    @Size(max = 45)
    @Column(name = "orderNote", length = 45)
    private String orderNote;

    @Size(max = 45)
    @Column(name = "orderStatus", length = 45)
    private String orderStatus;

}