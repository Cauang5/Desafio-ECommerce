package com.compass.ecommerce.controller;

import com.compass.ecommerce.DTO.ProductDTORequest;
import com.compass.ecommerce.DTO.ProductDTOResponse;
import com.compass.ecommerce.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTOResponse> createProduct(@Valid @RequestBody ProductDTORequest productDTORequest){
        ProductDTOResponse productDTOResponse = productService.create(productDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> getProducsById(@PathVariable Long id){
        ProductDTOResponse productDTOResponse = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productDTOResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTORequest productDTORequest){
        ProductDTOResponse product = productService.updateProduct(id, productDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
