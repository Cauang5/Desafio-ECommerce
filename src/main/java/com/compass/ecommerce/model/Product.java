package com.compass.ecommerce.model;

import com.compass.ecommerce.Exception.ResourceNotFoundException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_PRODUCT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private Integer stock;

    public Integer updateStock(int quantityChange) {
        if (this.stock + quantityChange < 0) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }

        return this.stock += quantityChange;
    }

    public void addStock(int quantityToAdd) {
        this.stock += quantityToAdd;
    }

    public void removeStock(int quantityToRemove) {
        if (this.stock - quantityToRemove < 0) {
            throw new ResourceNotFoundException("Estoque insuficiente para remover essa quantidade." +
                    "Quantidade máxima para ser adicionada à venda é de: " +getStock()+ " produtos");
        }
        this.stock -= quantityToRemove;
    }

}
