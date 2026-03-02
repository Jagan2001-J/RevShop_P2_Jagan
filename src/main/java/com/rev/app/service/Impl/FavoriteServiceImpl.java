package com.rev.app.service.Impl;

import com.rev.app.entity.Favorite;
import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import com.rev.app.repository.IFavoriteRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FavoriteServiceImpl implements IFavoriteService {

    @Autowired
    private IFavoriteRepository favoriteRepo;
    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private IProductRepository productRepo;

    @Override
    public void addToFavorites(Long userId, Long productId) {
        log.info("Adding product {} to favorites for user {}", productId, userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (!isFavorite(userId, productId)) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            favoriteRepo.save(favorite);
            log.debug("Saved new favorite record.");
        }
    }

    @Override
    public void removeFromFavorites(Long userId, Long productId) {
        log.info("Removing product {} from favorites for user {}", productId, userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Favorite> favorite = favoriteRepo.findByUserAndProduct(user, product);
        favorite.ifPresent(f -> {
            favoriteRepo.delete(f);
            log.debug("Deleted favorite record.");
        });
    }

    @Override
    public List<Favorite> getFavoritesByUserId(Long userId) {
        log.debug("Fetching favorites for user {}", userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return favoriteRepo.findByUser(user);
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return favoriteRepo.findByUserAndProduct(user, product).isPresent();
    }
}
