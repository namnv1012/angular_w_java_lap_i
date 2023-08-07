package com.laptop.shopping.dto;

import lombok.*;

import java.time.Instant;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProducerDto {

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
