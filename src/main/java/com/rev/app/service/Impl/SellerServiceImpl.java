package com.rev.app.service.Impl;

import com.rev.app.dto.RegisterRequestDTO;
import com.rev.app.dto.SellerResponseDTO;
import com.rev.app.entity.Seller;
import com.rev.app.entity.User;
import com.rev.app.mapper.SellerMapper;
import com.rev.app.repository.ISellerRepository;
import com.rev.app.service.Interface.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements ISellerService {

    @Autowired
    private ISellerRepository sellerRepository;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public SellerResponseDTO createSellerProfile(User user, RegisterRequestDTO request) {

        // Ensure a user doesn't already have a seller profile
        if (sellerRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User already has a registered seller profile");
        }

        // Assuming business name comes from the DTO name or a default setting since a
        // custom SellerRequestDTO wasn't listed
        String businessName = request.getName() != null ? request.getName() + "'s Shop" : "New Seller Shop";

        Seller seller = Seller.builder()
                .user(user)
                .businessName(businessName)
                .businessDetails("New business managed by " + user.getName())
                .build();

        Seller savedSeller = sellerRepository.save(seller);

        return sellerMapper.toDto(savedSeller);
    }

    @Override
    public SellerResponseDTO getSellerProfile(User user) {
        Seller seller = sellerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No seller profile found for this user"));

        return sellerMapper.toDto(seller);
    }
}
