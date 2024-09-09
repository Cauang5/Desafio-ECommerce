package com.compass.ecommerce.controller;

import com.compass.ecommerce.DTO.Product.GetProductDTOResponse;
import com.compass.ecommerce.DTO.Product.ProductDTORequest;
import com.compass.ecommerce.DTO.Product.ProductDTOResponse;
import com.compass.ecommerce.DTO.Product.UpdateStockDTORequest;
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
    public ResponseEntity<GetProductDTOResponse> getProducsById(@PathVariable Long id){
        GetProductDTOResponse productDTOResponse = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productDTOResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTORequest productDTORequest){
        ProductDTOResponse updateProduct = productService.updateProduct(id, productDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @PutMapping("/{id}/addStock")
    public ResponseEntity<ProductDTOResponse> addStock(@PathVariable Long id, @Valid @RequestBody UpdateStockDTORequest updateStockDTORequest) {
        ProductDTOResponse updatedProduct = productService.addProductStock(id, updateStockDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @PutMapping("/{id}/removeStock")
    public ResponseEntity<ProductDTOResponse> removeStock(@PathVariable Long id, @Valid @RequestBody UpdateStockDTORequest updateStockDTORequest) {
        ProductDTOResponse updatedProduct = productService.removeProductStock(id, updateStockDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
