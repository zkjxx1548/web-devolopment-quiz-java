package com.thoughtworks.web.quiz.controller;

import com.thoughtworks.web.quiz.domian.Product;
import com.thoughtworks.web.quiz.dto.ProductDto;
import com.thoughtworks.web.quiz.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProductControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();


    }

    @Test
    void should_return_products_list_when_get() throws Exception {
        ProductDto productDto1 =
                ProductDto.builder()
                        .productName("可乐1")
                        .price(1)
                        .unit("瓶")
                        .build();
        ProductDto productDto2 =
                ProductDto.builder()
                        .productName("可乐2")
                        .price(2)
                        .unit("瓶")
                        .build();
        ProductDto productDto3 =
                ProductDto.builder()
                        .productName("可乐3")
                        .price(3)
                        .unit("瓶")
                        .build();
        productRepository.save(productDto1);
        productRepository.save(productDto2);
        productRepository.save(productDto3);

        mockMvc.perform(get("/"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].productName", is("可乐1")))
                .andExpect(jsonPath("$[0].price", is(1)))
                .andExpect(jsonPath("$[0].unit", is("瓶")))
                .andExpect(status().isOk());
    }
}