package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "sale_items_seller")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @Size(max = 60)
    @NotNull
    @Column(name = "model", nullable = false, length = 60)
    private String model;

    @NotNull
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "screenSizeInch", precision = 7, scale = 2)
    private BigDecimal screenSizeInch;

    @Column(name = "ramGb")
    private Integer ramGb;

    @Column(name = "storageGb")
    private Integer storageGb;

    @Size(max = 45)
    @Column(name = "color", length = 45)
    private String color;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false )
    private Instant createdOn;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false )
    private Instant updatedOn;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerId", nullable = false)
    private User seller;

}