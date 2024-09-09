package com.compass.ecommerce.model.initializr;

import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.ItemSale;
import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.model.Sale;
import com.compass.ecommerce.model.User;
import com.compass.ecommerce.model.enums.SaleStatus;
import com.compass.ecommerce.model.enums.UserRole;
import com.compass.ecommerce.repository.ItemSaleRepository;
import com.compass.ecommerce.repository.ProductRepository;
import com.compass.ecommerce.repository.SaleRepository;
import com.compass.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ItemSaleRepository itemSaleRepository;


    @Override
    public void run(String... args) throws Exception {
        // Verifica se o banco já possui dados
        if (productRepository.count() == 0) {
            // Insere produtos no banco de dados
            Product productA = new Product();
            productA.setName("Mouse");
            productA.setDescription("Mouse Gamer");
            productA.setPrice(new BigDecimal("149.90"));
            productA.setStock(50);
            productA.setQuantity(0);

            Product productB = new Product();
            productB.setName("Garrafa");
            productB.setDescription("Garrafa termica 500ml");
            productB.setPrice(new BigDecimal("20.00"));
            productB.setStock(300);
            productB.setQuantity(0);

            productRepository.save(productA);
            productRepository.save(productB);

            String encryptedPassword = new BCryptPasswordEncoder().encode("123456789");


            User userA = new User();
            userA.setName("Artur Antunes");
            userA.setLogin("artur");
            userA.setEmail("artur@gmail.com");
            userA.setPassword(encryptedPassword);
            userA.setRole(UserRole.ADMIN);

            userRepository.save(userA);

            User userB = new User();
            userB.setName("joao pedro");
            userB.setLogin("joaopedro");
            userB.setEmail("joaopedro@gmail.com");
            userB.setPassword(encryptedPassword);
            userB.setRole(UserRole.USER);

            userRepository.save(userB);

            User userC = new User();
            userC.setName("maria vitoria");
            userC.setLogin("mavi");
            userC.setEmail("mavi@gmail.com");
            userC.setPassword(encryptedPassword);
            userC.setRole(UserRole.USER);

            userRepository.save(userC);

            ///////////////////////////////////////// SALE //////////////////////////////////////////////////////////////

            Sale saleA = new Sale();
            saleA.setUser(userC);

            LocalDateTime specificDate = LocalDateTime.of(2024, 8, 15, 14, 30);
            saleA.setDate(specificDate);
            saleA.setStatus(SaleStatus.PROCESSING);


            List<ItemSale> itemSales = new ArrayList<>();
            Product product = productRepository.findById(productB.getId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));


            ItemSale itemA = new ItemSale();
            itemA.setProduct(product);
            itemA.setQuantity(4);
            itemA.setSale(saleA);
            itemSales.add(itemA);

            saleA.setItemSales(itemSales);

            BigDecimal total = itemSales.stream()
                    .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            saleA.setTotal(total);
            saleRepository.save(saleA);

            ///////////////////////////////////////// NEW SALE //////////////////////////////////////////////////////////////

            Sale saleB = new Sale();
            saleB.setUser(userC);

            LocalDateTime specificDateB = LocalDateTime.of(2024, 8, 12, 14, 30);
            saleB.setDate(specificDateB);
            saleB.setStatus(SaleStatus.PROCESSING);


            List<ItemSale> itemSalesB = new ArrayList<>();
            Product productBb = productRepository.findById(productA.getId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));


            ItemSale itemB = new ItemSale();
            itemB.setProduct(productBb);
            itemB.setQuantity(2);
            itemB.setSale(saleB);
            itemSalesB.add(itemB);

            saleB.setItemSales(itemSalesB);

            BigDecimal totalB = itemSalesB.stream()
                    .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            saleB.setTotal(totalB);
            saleRepository.save(saleB);


            ///////////////////////////////////////// NEW SALE //////////////////////////////////////////////////////////////

            Sale saleC = new Sale();
            saleC.setUser(userC);

            LocalDateTime specificDateC = LocalDateTime.of(2024, 8, 31, 14, 30);
            saleC.setDate(specificDateC);
            saleC.setStatus(SaleStatus.PROCESSING);


            List<ItemSale> itemSalesC = new ArrayList<>();
            Product productCc = productRepository.findById(productA.getId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));


            ItemSale itemC = new ItemSale();
            itemC.setProduct(productCc);
            itemC.setQuantity(2);
            itemC.setSale(saleC);
            itemSalesC.add(itemC);

            saleC.setItemSales(itemSalesC);

            BigDecimal totalC = itemSalesC.stream()
                    .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            saleC.setTotal(totalC);
            saleRepository.save(saleC);
        }
    }
}


