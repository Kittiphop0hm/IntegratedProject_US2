package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "countryOfOrigin", length = 80)
    private String countryOfOrigin;

    @Column(name = "webSiteUrl", length = 40)
    private String webSiteUrl;

    @ColumnDefault("0")
    @Column(name = "isActive")
    private Byte isActive;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", nullable = false , insertable = false, updatable = false)
    private Instant createdOn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", nullable = false , insertable = false, updatable = false)
    private Instant updatedOn;

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    private Set<SaleItem> saleItems = new LinkedHashSet<>();

}