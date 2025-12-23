package com.ecommerce.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Long price = 0L;

  @Column(nullable = false)
  private int stock = 0;

  // if using dependecies lombok is not constructor, getter and setter.
  public Product() {
  }

  public Product(Long id, String name, Long price, int stock) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }
}
