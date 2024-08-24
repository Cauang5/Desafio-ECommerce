package com.compass.ecommerce.Service;

import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product){
        productRepository.save(product);
        return product;
    }

    public List<Product> getAllProducts(){
        Sort sort = Sort.by("id").ascending(); //Ordenação para retornar os produtos em pelo ID
        return productRepository.findAll(sort);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontado pelo Id " +id));
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product productUpdated){
        Product product = getProductById(id);
        product.setName(productUpdated.getName());
        product.setDescription(productUpdated.getDescription());
        product.setPrice(productUpdated.getPrice());
        product.setQuantity(productUpdated.getQuantity());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
        Product product = getProductById(id);
        productRepository.delete(product);
    }

}
