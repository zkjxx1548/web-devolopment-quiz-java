package com.thoughtworks.web.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class ProductDto {
    @Id
    @GeneratedValue
    private int id;
    private String productName;
    private int price;
    private String url;
    private String unit;
}
