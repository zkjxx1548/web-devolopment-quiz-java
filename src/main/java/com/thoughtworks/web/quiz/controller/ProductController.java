package com.thoughtworks.web.quiz.controller;

import com.thoughtworks.web.quiz.domian.Product;
import com.thoughtworks.web.quiz.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/")
    public ResponseEntity<List<Product>> getProductList() {
        List<Product> products =
            productRepository.findAll().stream()
                .map(
                        productDto ->
                                Product.builder()
                                        .name(productDto.getProductName())
                                        .price(productDto.getPrice())
                                        .unit(productDto.getUnit())
                                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}
