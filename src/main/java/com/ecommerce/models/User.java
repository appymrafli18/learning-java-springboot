package com.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Membuat constructor kosong
@AllArgsConstructor // Membuat constructor dengan semua parameter
@Builder // Memungkinkan pembuatan object dengan gaya User.builder().name("Andi").build()
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant created;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updated;
}
