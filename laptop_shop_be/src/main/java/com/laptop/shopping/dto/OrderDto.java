package com.laptop.shopping.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private String userCode;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String note;

    private Integer status;

    private Instant orderDate;

    List<OrderItemDto> lstOrderItem;
}
