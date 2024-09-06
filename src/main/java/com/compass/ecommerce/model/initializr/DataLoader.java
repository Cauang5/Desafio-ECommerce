package com.compass.ecommerce.model.initializr;

import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.model.User;
import com.compass.ecommerce.model.enums.UserRole;
import com.compass.ecommerce.repository.ProductRepository;
import com.compass.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        // Verifica se o banco já possui dados
        if (productRepository.count() == 0) {
            // Insere produtos no banco de dados
            Product productA = new Product();
            productA.setName("Mouse");
            productA.setDescription("Mouse Gamer");
            productA.setPrice(new BigDecimal("149.90"));
            productA.setQuantity(50);

            Product productB = new Product();
            productB.setName("Garrafa");
            productB.setDescription("Garrafa termica 500ml");
            productB.setPrice(new BigDecimal("20.00"));
            productB.setQuantity(300);

            productRepository.save(productA);
            productRepository.save(productB);

            String encryptedPassword = new BCryptPasswordEncoder().encode("123456789");


            User userA = new User();
            userA.setName("Arrascaeta Gerson");
            userA.setLogin("arraxca");
            userA.setEmail("arrascage@gmail.com");
            userA.setPassword(encryptedPassword);
            userA.setRole(UserRole.ADMIN);

            userRepository.save(userA);

            User userB = new User();
            userB.setName("jose paulo");
            userB.setLogin("josepaulo");
            userB.setEmail("josepaulo@gmail.com");
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
        }
    }
}
