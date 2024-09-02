package com.compass.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_SALE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "A data da venda não pode ser no passado.")
    private LocalDateTime date;

    @NotNull(message = "O total da venda é obrigatório.")
    @DecimalMin(value = "0.01", message = "O total da venda deve ser maior que zero.")
    private BigDecimal total;

    @NotNull(message = "O usuário é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User user;

    @NotEmpty(message = "A venda deve conter pelo menos um item.")
    @Size(min = 1, message = "A venda deve conter pelo menos um item.")
    @OneToMany(mappedBy = "sale")
    private List<ItemSale> itemSales;

}
