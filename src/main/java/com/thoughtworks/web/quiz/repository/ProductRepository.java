package com.thoughtworks.web.quiz.repository;

import com.thoughtworks.web.quiz.dto.ProductDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<ProductDto, Integer> {
    @Override
    List<ProductDto> findAll();
}
