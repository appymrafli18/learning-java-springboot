package com.ecommerce.entity;

// import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Getter
@Setter
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

  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  @JsonIgnore
  private Category category;

  // tidak perlu, karena tidak ingin di akses dri product
//  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<CartItem> cartItem;
}
