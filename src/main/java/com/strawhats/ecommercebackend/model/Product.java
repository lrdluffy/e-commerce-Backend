package com.strawhats.ecommercebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageUrl;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews =  new ArrayList<>();
}
