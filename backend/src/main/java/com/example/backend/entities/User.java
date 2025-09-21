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
@Table(name = "account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "fullname", nullable = false, length = 45)
    private String fullName;

    @Size(max = 45)
    @NotNull
    @Column(name = "nickname", nullable = false, length = 45)
    private String nickName;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Column(name = "isActive")
    private Boolean isActive;

    @Size(max = 45)
    @NotNull
    @Column(name = "accountType", nullable = false, length = 45)
    private String userType;

    @Size(max = 45)
    @Column(name = "bankName", length = 45)
    private String bankName;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Instant createdOn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Instant updatedOn;

    @Size(max = 45)
    @Column(name = "mobile", length = 45)
    private String phoneNumber;

    @Size(max = 45)
    @Column(name = "bankAccountNo", length = 45)
    private String bankAccount;

    @Size(max = 45)
    @Column(name = "cardNo", length = 45)
    private String cardNumber;
}