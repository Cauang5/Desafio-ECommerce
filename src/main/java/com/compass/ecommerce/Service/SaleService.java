package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.ItemSaleDTOResponse;
import com.compass.ecommerce.DTO.SaleDTORequest;
import com.compass.ecommerce.DTO.SaleDTOResponse;
import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.ItemSale;
import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.model.Sale;
import com.compass.ecommerce.model.User;
import com.compass.ecommerce.model.enums.SaleStatus;
import com.compass.ecommerce.repository.ItemSaleRepository;
import com.compass.ecommerce.repository.ProductRepository;
import com.compass.ecommerce.repository.SaleRepository;
import com.compass.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public SaleDTOResponse createSale(SaleDTORequest saleDTORequest) {
        User user = userRepository.findById(saleDTORequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Código do cliente inválido"));


        Sale sale = new Sale();
        sale.setUser(user);
        sale.setDate(LocalDateTime.now());

        //Converte os itens da requisição em uma lista de ItemSale
        List<ItemSale> itemSales = saleDTORequest.items().stream()
                .map(itemDTO -> {
                    // Verifica se o ID do produto é nulo
                    if (itemDTO.productId() == null) {
                        throw new IllegalArgumentException("O id do produto não pode ser nulo");
                    }

                    // Busca o produto pelo ID
                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + itemDTO.productId()));


                    if (product.getQuantity() < itemDTO.quantity()){
                        throw new RuntimeException("Estoque insuficiente do produto: " +product.getName());
                    }

                    // Cria um novo ItemSale e o associa ao produto e à venda
                    ItemSale itemSale = new ItemSale();

                    itemSale.setProduct(product);

                    itemSale.setQuantity(itemDTO.quantity());
                    itemSale.setSale(sale);

                    return itemSale;

                })
                .collect(Collectors.toList());

        // Lógica para controle do estoque
        for(ItemSale itemSale :itemSales){
            Product product = itemSale.getProduct();

            Integer newQuantity = product.getQuantity() - itemSale.getQuantity();
            product.setQuantity(newQuantity);

            productRepository.save(product);
        }

        itemSaleRepository.saveAll(itemSales);
        sale.setItemSales(itemSales);

        //Realiza o calculo dos itens a venda
        BigDecimal total = itemSales.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setTotal(total);

        saleRepository.save(sale);

        return new SaleDTOResponse(
                sale.getId(),
                sale.getUser().getName(),
                sale.getDate(),
                sale.getStatus(),
                sale.getTotal(),
                sale.getItemSales().stream()
                        .map(itemSale -> new ItemSaleDTOResponse(
                                itemSale.getProduct().getId(),
                                itemSale.getProduct().getName(),
                                itemSale.getProduct().getDescription(),
                                itemSale.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public SaleDTOResponse findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada pelo id: " +id));

        List<ItemSaleDTOResponse> items = sale.getItemSales().stream()
                .map(itemSale -> new ItemSaleDTOResponse(
                        itemSale.getProduct().getId(),
                        itemSale.getProduct().getName(),
                        itemSale.getProduct().getDescription(),
                        itemSale.getQuantity()

                ))
                .collect(Collectors.toList());

        return new SaleDTOResponse(
                sale.getId(),
                sale.getUser().getName(),
                sale.getDate(),
                sale.getStatus(),
                sale.getTotal(),
                items);

    }

    public SaleDTOResponse updateSale(Long id, SaleDTORequest saleDTORequest) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada pelo id: " +id));

        User user = userRepository
                .findById(saleDTORequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo ID: " + saleDTORequest.userId()));

        sale.setUser(user);
        sale.setDate(LocalDateTime.now());

        //Remove todos os itens associados à venda
        sale.getItemSales().forEach(itemSale -> itemSaleRepository.delete(itemSale));

        List<ItemSale> updatedItemSales = saleDTORequest.items().stream()
                .map(itemDTO -> {
                    if (itemDTO.productId() == null) {
                        throw new IllegalArgumentException("O id do produto não pode ser nulo");
                    }

                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + itemDTO.productId()));

                    ItemSale itemSale = new ItemSale();
                    itemSale.setProduct(product);
                    itemSale.setQuantity(itemDTO.quantity());
                    itemSale.setSale(sale);

                    return itemSale;
                })
                .collect(Collectors.toList());

        //Verifica o status atual da venda e atualiza o estoque
        SaleStatus saleStatus = saleDTORequest.status();
        sale.setStatus(saleStatus);

        for(ItemSale itemSale :updatedItemSales){
            Product product = itemSale.getProduct();

            if (saleStatus == SaleStatus.CANCELED) {
                // Caso a venda seja cancelada, devolve o produto ao estoque
                Integer newQuantity = product.getQuantity() + itemSale.getQuantity();
                product.setQuantity(newQuantity);
            } else if (saleStatus == SaleStatus.FINISHED) {
                // Caso a venda seja finalizada, remove permanentemente o produto do estoque
                Integer newQuantity = product.getQuantity() - itemSale.getQuantity();
                if (newQuantity < 0) {
                    throw new RuntimeException("Estoque insuficiente para o produto: " + product.getName());
                }
                product.setQuantity(newQuantity);
            }

            productRepository.save(product);
        }

        itemSaleRepository.saveAll(updatedItemSales);
        sale.setItemSales(updatedItemSales);

        BigDecimal total = updatedItemSales.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setTotal(total);

        saleRepository.save(sale);

        return new SaleDTOResponse(
                sale.getId(),
                sale.getUser().getName(),
                sale.getDate(),
                sale.getStatus(),
                sale.getTotal(),
                sale.getItemSales().stream()
                        .map(itemSale -> new ItemSaleDTOResponse(
                                itemSale.getProduct().getId(),
                                itemSale.getProduct().getName(),
                                itemSale.getProduct().getDescription(),
                                itemSale.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada pelo id: " +id));

        //Remove todos os itens associados à venda
        sale.getItemSales().forEach(itemSale -> itemSaleRepository.delete(itemSale));

        saleRepository.delete(sale);
    }
}
