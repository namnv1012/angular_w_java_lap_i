package com.laptop.shopping.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {
    private int status;
    private String message;
    private T data;
    private long totalData;
    private int totalPage;
    private int currentPage;
}
