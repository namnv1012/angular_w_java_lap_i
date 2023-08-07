package com.laptop.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;
    private Long orderId;
    private String productCode;
    private String productName;
    private Float productPrice;
    private Integer quantity;
    private Float price;
    // thêm 2 trường số lương và tổng giá cho thống kê
    private Integer amount;
    private Integer totalPrice;
}
