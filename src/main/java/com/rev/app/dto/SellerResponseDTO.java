package com.rev.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDTO {
    private Long id;
    private String businessName; // Can be derived from User.name or separate entity field
    private String sellerName;
    private String email;
}
