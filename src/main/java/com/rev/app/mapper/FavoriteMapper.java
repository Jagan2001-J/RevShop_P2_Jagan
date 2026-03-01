package com.rev.app.mapper;

import com.rev.app.dto.FavoriteResponseDTO;
import com.rev.app.entity.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    @Autowired
    private ProductMapper productMapper;

    public FavoriteResponseDTO toDto(Favorite favorite) {
        if (favorite == null) {
            return null;
        }

        return FavoriteResponseDTO.builder()
                .id(favorite.getId())
                .product(productMapper.toDto(favorite.getProduct()))
                .build();
    }
}
