package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "fileName", nullable = false, length = 200)
    private String fileName;

    @NotNull
    @Column(name = "imageViewOrder", nullable = false)
    private Integer imageViewOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesId")
    private SaleItem sales;
}