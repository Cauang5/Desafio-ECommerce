package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.ItemSaleDTO;
import com.compass.ecommerce.DTO.SaleDTO;
import com.compass.ecommerce.model.ItemSale;
import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.model.Sale;
import com.compass.ecommerce.repository.ItemSaleRepository;
import com.compass.ecommerce.repository.ProductRepository;
import com.compass.ecommerce.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final ProductRepository productRepository;

    public Sale createSale(SaleDTO saleDTO) {

        var sale = new Sale();
        sale.setDate(LocalDateTime.now());


        List<ItemSale> itensSale = convertItens(sale, saleDTO.itens());
        sale.setItens(itensSale);

        BigDecimal totalValue = calculateTotalPrice(sale.getItens());
        sale.setTotal(totalValue);


        saleRepository.save(sale);
        itemSaleRepository.saveAll(itensSale);

        return sale;
    }

    public SaleDTO findSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id não encontrado" + id));

        List<ItemSaleDTO> itensDTO = sale.getItens()
                .stream()
                .map(itemSale -> new ItemSaleDTO(
                        itemSale.getProduct().getId(),
                        itemSale.getProduct().getName(),
                        itemSale.getProduct().getDescription(),
                        itemSale.getQuantity()
                ))
                .collect(Collectors.toList());

        return new SaleDTO(
                sale.getTotal(),
                itensDTO
        );
    }


    public Sale updateSale(Long id, SaleDTO saleDTO) {
        System.out.println("procurando venda");
        Sale saleExistent = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        //saleExistent.getItens().clear();

        saleExistent.setItens(convertItens(saleExistent, saleDTO.itens()));

        /*List<ItemSale> updatedItems = convertItens(saleExistent, saleDTO.itens());
        saleExistent.setItens(updatedItems);*/

        BigDecimal total = calculateTotalPrice(saleExistent.getItens());
        saleExistent.setTotal(total);

        return saleRepository.save(saleExistent);
    }


    //Método para calcular os valores dos itens adicionados à venda.
    private BigDecimal calculateTotalPrice(List<ItemSale> itemSale) {
        return itemSale.stream()
                .map(item -> {
                    BigDecimal priceItemSale = item.getProduct().getPrice();
                    return priceItemSale.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    //Método para converter uma Lista de ItemSaleDTO em itemSale
    private List<ItemSale> convertItens(Sale sale, List<ItemSaleDTO> itens) {
        if (itens.isEmpty()) {
            throw new RuntimeException("Não é possível realizar um pedido sem item");
        }
        return itens
                .stream()
                .map(dto -> {
                    Long idItem = dto.productId();
                    Product product = productRepository
                            .findById(idItem)
                            .orElseThrow(() -> new RuntimeException("Código de produto inválido" + idItem));
                    ItemSale itemSale = new ItemSale();
                    itemSale.setQuantity(dto.quantity());
                    itemSale.setSale(sale);
                    itemSale.setProduct(product);

                    System.out.println("Produto associado " + product.getName() + " com o id: " + product.getId());

                    return itemSale;
                }).collect(Collectors.toList());
    }
}
