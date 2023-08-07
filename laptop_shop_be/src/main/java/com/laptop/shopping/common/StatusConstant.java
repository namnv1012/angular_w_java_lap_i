package com.laptop.shopping.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConstant {
    ACTIVE(1L), INACTIVE(0L);
    Long value;
}
