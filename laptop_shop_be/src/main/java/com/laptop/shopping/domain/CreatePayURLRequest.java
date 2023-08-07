package com.laptop.shopping.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayURLRequest {

    @NotBlank
    private String vnp_Version;

    @NotBlank
    private String vnp_Command;

    @NotNull
    @Min(value = 1)
    private Integer vnp_Amount;

    @NotBlank
    private String vnp_Locale;
}
