package com.compass.ecommerce.controller;

import com.compass.ecommerce.DTO.Sale.SaleDTORequest;
import com.compass.ecommerce.DTO.Sale.SaleDTOResponse;
import com.compass.ecommerce.Service.SaleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @PutMapping("/{saleId}/confirm")
    public ResponseEntity<SaleDTOResponse> confirmSale(@PathVariable Long saleId) {
        SaleDTOResponse confirmedSale = saleService.confirmSale(saleId);
        return ResponseEntity.status(HttpStatus.OK).body(confirmedSale);
    }

    @PutMapping("/{saleId}/cancel")
    public ResponseEntity<SaleDTOResponse> cancelSale(@PathVariable Long saleId) {
        SaleDTOResponse cancelledSale = saleService.cancelSale(saleId);
        return ResponseEntity.status(HttpStatus.OK).body(cancelledSale);
    }

    @GetMapping("/report-weekly")
    public List<SaleDTOResponse> weeklyReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return saleService.weeklyReport(date);
    }
}
