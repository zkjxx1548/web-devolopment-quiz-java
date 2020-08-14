package com.thoughtworks.web.quiz.domian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private String name;
    private int price;
    private String url;
    private String unit;
}
