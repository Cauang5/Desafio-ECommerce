package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.Product.GetProductDTOResponse;
import com.compass.ecommerce.DTO.Product.ProductDTORequest;
import com.compass.ecommerce.DTO.Product.ProductDTOResponse;
import com.compass.ecommerce.DTO.Product.UpdateStockDTORequest;
import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.Product;
import com.compass.ecommerce.model.Sale;
import com.compass.ecommerce.model.enums.SaleStatus;
import com.compass.ecommerce.repository.ItemSaleRepository;
import com.compass.ecommerce.repository.ProductRepository;
import com.compass.ecommerce.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ItemSaleRepository itemSaleRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDTOResponse create(ProductDTORequest productDTORequest) {
        Product product = new Product();
        product.setName(productDTORequest.name());
        product.setDescription(productDTORequest.description());
        product.setPrice(productDTORequest.price());
        product.setQuantity(productDTORequest.quantity());
        product.setStock(productDTORequest.quantity());

        productRepository.save(product);

        return new ProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStock());
    }

    public GetProductDTOResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + id));

        return new GetProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock());
    }

    @Cacheable(value = "products")
    public List<GetProductDTOResponse> getAllProducts() {
        System.out.println("Consultando o banco de dados para obter todos os produtos.");
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new GetProductDTOResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock()))
                .collect(Collectors.toList());
    }



    public ProductDTOResponse updateProduct(Long id, ProductDTORequest productDTORequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + id));

        product.setName(productDTORequest.name());
        product.setDescription(productDTORequest.description());
        product.setPrice(productDTORequest.price());
        product.setQuantity(productDTORequest.quantity());
        product.setStock(productDTORequest.quantity());

        productRepository.save(product);

        return new ProductDTOResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStock()
        );
    }

    // Adiciona produtos ao estoque
    public ProductDTOResponse addProductStock(Long id ,UpdateStockDTORequest addProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + id));

        product.addStock(addProduct.quantity());

        productRepository.save(product);

        return new ProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStock()
        );
    }

    // Remove produtos do estoque
    public ProductDTOResponse removeProductStock(Long id, UpdateStockDTORequest removeStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + id));

        product.removeStock(removeStock.quantity());

        productRepository.save(product);

        return new ProductDTOResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStock()
        );
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado pelo id: " + id));


        List<Sale> salesWithProduct = saleRepository.findAllByProductIdAndStatus(id, SaleStatus.PROCESSING);
        if (!salesWithProduct.isEmpty()) {
            throw new ResourceNotFoundException("Não é possível excluir o produto enquanto houver vendas em processamento associadas.");
        }

        itemSaleRepository.deleteByProductId(id);

        productRepository.delete(product);
    }

}
