package com.compass.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min =  2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
    private String name;

    @NotNull(message = "A descrição não pode ser nula")
    @Size(min = 5, max = 255, message = "A descrição deve ter entre 5 e 255 caracteres")
    private String description;

    @NotNull(message = "O preço não pode ser nulo ")
    @Positive(message = "O Preço deverá ser positivo")
    private BigDecimal price;

    @NotNull(message = "A quantidade não pode ser nula")
    @Min(value = 1, message = "A quantidade mínima deve ser 1")
    private Integer quantity;

}
