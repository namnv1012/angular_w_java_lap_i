package com.laptop.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String code;
    private String name;
    private Boolean status;
    private String statusStr;
    private Instant createDate;
    private String createName;
    private Instant updateDate;
    private String updateName;
}
