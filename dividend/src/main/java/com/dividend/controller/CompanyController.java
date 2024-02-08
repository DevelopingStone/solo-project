package com.dividend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> searchCompany() {
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    public ResponseEntity<?> addCompany() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteCompany() {
        return ResponseEntity.ok().build();
    }

}
