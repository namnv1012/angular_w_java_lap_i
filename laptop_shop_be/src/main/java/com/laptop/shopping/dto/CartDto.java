package com.laptop.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long id;
    private String userCode;
    private String productCode;
    private String productName;
    private String productContent;
    private Float price;
    private Integer quantity;
}
