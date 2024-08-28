package com.compass.ecommerce.controller;

import com.compass.ecommerce.DTO.SaleDTO;
import com.compass.ecommerce.Service.SaleService;
import com.compass.ecommerce.model.Sale;
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
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDTO){
        Sale sale = saleService.createSale(saleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findByID(@PathVariable Long id){
        SaleDTO sale = saleService.findSaleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable Long id, @RequestBody SaleDTO saleDTO){
        Sale sale = saleService.updateSale(id, saleDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<SaleDTO> deleteSale(@PathVariable Long id){
        saleService.deleteSale(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/
}
