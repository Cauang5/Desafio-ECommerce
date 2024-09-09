package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.ItemSale.ItemSaleDTOResponse;
import com.compass.ecommerce.DTO.Sale.SaleDTORequest;
import com.compass.ecommerce.DTO.Sale.SaleDTOResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                .orElseThrow(() -> new ResourceNotFoundException("Código do usuário inválido"));


        Sale sale = new Sale();
        sale.setUser(user);
        sale.setDate(LocalDateTime.now());
        sale.setStatus(SaleStatus.PROCESSING);

        //Converte os itens da requisição em uma lista de ItemSale
        List<ItemSale> itemSales = saleDTORequest.items().stream()
                .map(itemDTO -> {
                    // Verifica se o ID do produto é nulo
                    if (itemDTO.productId() == null) {
                        throw new ResourceNotFoundException("O id do produto não pode ser nulo");
                    }

                    // Busca o produto pelo ID
                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + itemDTO.productId()));

                    System.out.println(product.getStock());

                    product.removeFromStock(itemDTO.stock());

                    // Cria um novo ItemSale e o associa ao produto e à venda
                    ItemSale itemSale = new ItemSale();

                    itemSale.setProduct(product);

                    itemSale.setQuantity(itemDTO.stock());
                    itemSale.setSale(sale);

                    return itemSale;

                })
                .collect(Collectors.toList());

        // Lógica para controle do estoque
        for (ItemSale itemSale : itemSales) {
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

    public SaleDTOResponse confirmSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada pelo id: " + id));

        // Pega o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User authenticatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!sale.getUser().getId().equals(authenticatedUser.getId()) && !isAdmin) {
            throw new ResourceNotFoundException("Você não tem permissão para confirmar esta venda");
        }

        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new ResourceNotFoundException("Não é possível finalizar uma venda que já foi cancelada.");
        }

        sale.setStatus(SaleStatus.FINISHED);
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

    public SaleDTOResponse cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada pelo id: " + id));

        // Pega o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User authenticatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!sale.getUser().getId().equals(authenticatedUser.getId()) && !isAdmin) {
            throw new ResourceNotFoundException("Você não tem permissão para cancelar esta venda");
        }

        if (sale.getStatus() == SaleStatus.FINISHED) {
            throw new ResourceNotFoundException("Não é possível cancelar uma venda que já foi finalizada.");
        }

        sale.setStatus(SaleStatus.CANCELED);

        for (ItemSale itemSale : sale.getItemSales()) {
            Product product = itemSale.getProduct();
            Integer restoredStock = product.getQuantity() + itemSale.getQuantity();
            product.setQuantity(restoredStock);
            product.setStock(product.updateStock(itemSale.getQuantity()));
            productRepository.save(product);
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada pelo id: " + id));

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
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada pelo id: " + id));

        // Pega o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User authenticatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!sale.getUser().getId().equals(authenticatedUser.getId()) && !isAdmin) {
            throw new ResourceNotFoundException("Você não tem permissão para Atualizar esta venda");
        }

        if (sale.getStatus() == SaleStatus.FINISHED) {
            throw new ResourceNotFoundException("Não é possível atualizar uma venda que já foi finalizada.");
        }

        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new ResourceNotFoundException("Não é possível atualizar uma venda que já foi cancelada.");
        }

        sale.setDate(LocalDateTime.now());

        //Remove todos os itens associados à venda
        sale.getItemSales().forEach(itemSale -> itemSaleRepository.delete(itemSale));

        List<ItemSale> updatedItemSales = saleDTORequest.items().stream()
                .map(itemDTO -> {
                    if (itemDTO.productId() == null) {
                        throw new ResourceNotFoundException("O id do produto não pode ser nulo");
                    }

                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + itemDTO.productId()));

                    ItemSale itemSale = new ItemSale();
                    itemSale.setProduct(product);
                    itemSale.setQuantity(itemDTO.stock());
                    itemSale.setSale(sale);

                    return itemSale;
                })
                .collect(Collectors.toList());

        sale.setStatus(sale.getStatus());

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
                .orElseThrow(() -> new RuntimeException("Venda não encontrada pelo id: " + id));

        //Remove todos os itens associados à venda
        sale.getItemSales().forEach(itemSale -> itemSaleRepository.delete(itemSale));

        saleRepository.delete(sale);
    }

    public List<SaleDTOResponse> weeklyReport(LocalDate date) {
        // Calcular o início e o fim da semana
        LocalDateTime startWeek = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endWeek = date.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);

        // Buscar as vendas no intervalo entre o início e o fim da semana
        List<Sale> sales = saleRepository.findSalesByDateRange(startWeek, endWeek);

        // Converter as vendas para DTOs de resposta
        return sales.stream()
                .map(sale -> new SaleDTOResponse(
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
                ))
                .collect(Collectors.toList());
    }

    public List<SaleDTOResponse> monthlyReport(LocalDate date) {
        // Calcular o início e o fim do mês
        LocalDateTime startMonth = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endMonth = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);

        // Buscar as vendas no intervalo entre o início e o fim do mês
        List<Sale> sales = saleRepository.findSalesByDateRange(startMonth, endMonth);

        // Converter as vendas para DTOs de resposta
        return sales.stream()
                .map(sale -> new SaleDTOResponse(
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
                ))
                .collect(Collectors.toList());
    }
}
