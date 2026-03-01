package com.rev.app.mapper;

import com.rev.app.dto.SellerResponseDTO;
import com.rev.app.entity.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {

    public SellerResponseDTO toDto(Seller seller) {
        if (seller == null) {
            return null;
        }

        String sellerName = seller.getUser() != null ? seller.getUser().getName() : null;
        String email = seller.getUser() != null ? seller.getUser().getEmail() : null;

        return SellerResponseDTO.builder()
                .id(seller.getId())
                .businessName(seller.getBusinessName())
                .sellerName(sellerName)
                .email(email)
                .build();
    }
}
