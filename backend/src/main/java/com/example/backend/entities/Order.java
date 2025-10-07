package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @Size(max = 100)
    @Column(name = "shippingAddress", length = 100)
    private String shippingAddress;

    @Size(max = 100)
    @Column(name = "orderNote", length = 100)
    private String orderNote;

    @Size(max = 100)
    @Column(name = "orderStatus", length = 100)
    private String orderStatus;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "paymentDate", nullable = false, insertable = false, updatable = false )
    private Instant paymentDate;

}
