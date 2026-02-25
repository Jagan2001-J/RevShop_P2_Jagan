package com.rev.app.service.Impl;

import com.rev.app.entity.Address;
import com.rev.app.repository.IAddressRepository;
import com.rev.app.service.Interface.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressRepository repo;

    @Override
    public Address addAddress(Address address) {
        return repo.save(address);
    }

    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public Address getAddressById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Override
    public void deleteAddress(Long id) {
        repo.deleteById(id);
    }
}
