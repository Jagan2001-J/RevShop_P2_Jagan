package com.rev.app.mapper;

import com.rev.app.dto.CategoryResponseDTO;
import com.rev.app.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toDto(Product.Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponseDTO.builder()
                .name(category.name())
                .build();
    }
}
