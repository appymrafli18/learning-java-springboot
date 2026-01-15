package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
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
  @JsonIgnore /* alternatif using @JsonBackReference */
  @Builder.Default // Penting agar ArrayList tidak menjadi null saat menggunakan Builder
  @OrderBy("id ASC")
  private List<Product> products = new ArrayList<>();
}
