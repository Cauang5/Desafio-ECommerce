package com.compass.ecommerce.controller;

import com.compass.ecommerce.DTO.SaleDTORequest;
import com.compass.ecommerce.DTO.SaleDTOResponse;
import com.compass.ecommerce.Service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/sale")
public class SaleController {

    private SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleDTOResponse> createSale(@Valid @RequestBody SaleDTORequest saleDTORequest){
        SaleDTOResponse sale = saleService.createSale(saleDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTOResponse> findByID(@PathVariable Long id){
        SaleDTOResponse sale = saleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTOResponse> updateSale(@PathVariable Long id, @Valid @RequestBody SaleDTORequest saleDTORequest){
        SaleDTOResponse saleDTOResponse = saleService.updateSale(id, saleDTORequest);
        return ResponseEntity.status(HttpStatus.OK).body(saleDTOResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SaleDTOResponse> deleteSale(@PathVariable Long id){
        saleService.deleteSale(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
