package com.rev.app.service.Interface;

import com.rev.app.entity.Favorite;
import java.util.List;

public interface IFavoriteService {
    void addToFavorites(Long userId, Long productId);

    void removeFromFavorites(Long userId, Long productId);

    List<Favorite> getFavoritesByUserId(Long userId);

    boolean isFavorite(Long userId, Long productId);
}
