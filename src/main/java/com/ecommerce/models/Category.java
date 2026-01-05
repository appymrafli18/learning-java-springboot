package com.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
// import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", orphanRemoval = false)
  @JsonBackReference /* alternatif using @JsonIgnore */
  @Builder.Default // Penting agar ArrayList tidak menjadi null saat menggunakan Builder
  private List<Product> products = new ArrayList<>();
}
