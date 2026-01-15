package com.ecommerce.entity;

import com.ecommerce.constants  .OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long total;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private OrderStatus status = OrderStatus.Pending;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant created_at;

  @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
  @Builder.Default
  @JsonIgnore
  private List<OrderItem> items = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  // helper
  public void addOrderItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);
  }
}
