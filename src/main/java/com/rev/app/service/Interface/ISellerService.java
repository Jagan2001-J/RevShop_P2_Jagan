package com.rev.app.service.Interface;

import com.rev.app.dto.SellerResponseDTO;
import com.rev.app.entity.User;

public interface ISellerService {
    SellerResponseDTO createSellerProfile(User user, com.rev.app.dto.RegisterRequestDTO request); // Overloading for
                                                                                                  // registration flow

    SellerResponseDTO getSellerProfile(User user);
    // SellerResponseDTO updateSellerProfile(User user, SellerRequestDTO request);
    // // Ignoring for now due to absent SellerRequestDTO
}
