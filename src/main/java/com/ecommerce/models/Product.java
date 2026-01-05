package com.ecommerce.models;

// import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Builder.Default
  private Long price = 0L;

  @Column(nullable = false)
  @Builder.Default
  private int stock = 0;

  @Column(nullable = true)
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;
}
