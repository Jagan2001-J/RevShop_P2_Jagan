package com.rev.app.service.Interface;

import com.rev.app.entity.Address;
import java.util.List;

public interface IAddressService {
    Address addAddress(Address address);

    List<Address> getAddressesByUserId(Long userId);

    Address getAddressById(Long id);

    void deleteAddress(Long id);
}
