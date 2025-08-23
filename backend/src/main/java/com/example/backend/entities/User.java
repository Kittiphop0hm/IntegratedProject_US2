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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "fullname", nullable = false, length = 45)
    private String fullname;

    @Size(max = 45)
    @NotNull
    @Column(name = "nickname", nullable = false, length = 45)
    private String nickname;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Size(max = 45)
    @NotNull
    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Size(max = 45)
    @NotNull
    @Column(name = "role", nullable = false, length = 45)
    private String role;

    @Size(max = 45)
    @Column(name = "mobile", length = 45)
    private String mobile;

    @Column(name = "bankAccountNumber")
    private Integer bankAccountNumber;

    @Column(name = "nationalCardNumber")
    private Integer nationalCardNumber;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Instant createdOn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Instant updatedOn;
}