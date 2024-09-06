package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.ProductDTORequest;
import com.compass.ecommerce.DTO.ProductDTOResponse;
import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTOResponse create(ProductDTORequest productDTORequest) {
        Product product = new Product();
        product.setName(productDTORequest.name());
        product.setDescription(productDTORequest.description());
        product.setPrice(productDTORequest.price());
        product.setQuantity(productDTORequest.quantity());

        productRepository.save(product);

        return new ProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity());
    }

    public ProductDTOResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " +id));

        return new ProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity());
    }

    public ProductDTOResponse updateProduct(Long id, ProductDTORequest productDTORequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " +id));

        product.setName(productDTORequest.name());
        product.setDescription(productDTORequest.description());
        product.setPrice(productDTORequest.price());
        product.setQuantity(productDTORequest.quantity());

        productRepository.save(product);

        return new ProductDTOResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity());
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " +id));

        productRepository.delete(product);
    }

}
