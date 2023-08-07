package com.laptop.shopping.dto;

import lombok.*;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyDetailsDto {

    private Long id;

    private Long warrantyId;

    private String productCode;

    private String productName;

    private String note;

    private Instant receivedDate;

    private Instant payDate;

    private Date dateReceived;

    private Date datePay;
}
