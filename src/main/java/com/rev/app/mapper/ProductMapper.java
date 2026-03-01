package com.rev.app.mapper;

import com.rev.app.dto.ProductRequestDTO;
import com.rev.app.dto.ProductResponseDTO;
import com.rev.app.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        // Note: The Category string from DTO should map to Product.Category enum in the
        // Service layer
        Product.Category category = null;
        try {
            if (dto.getCategory() != null) {
                category = Product.Category.valueOf(dto.getCategory().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            // Log or handle invalid category gracefully
        }

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .discountedPrice(dto.getDiscountedPrice())
                .quantity(dto.getQuantity())
                .imageUrl(dto.getImageUrl())
                .category(category)
                .build();
    }

    public ProductResponseDTO toDto(Product product) {
        if (product == null) {
            return null;
        }

        String sellerName = product.getSeller() != null ? product.getSeller().getName() : "Unknown Seller";

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory() != null ? product.getCategory().name() : null)
                .sellerName(sellerName)
                .build();
    }
}
