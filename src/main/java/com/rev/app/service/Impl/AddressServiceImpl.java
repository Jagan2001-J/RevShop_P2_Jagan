package com.rev.app.service.Impl;

import com.rev.app.entity.Address;
import com.rev.app.repository.IAddressRepository;
import com.rev.app.service.Interface.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressRepository repo;

    @Override
    public Address addAddress(Address address) {
        log.info("Saving new address for user ID: {}", address.getUser().getId());
        return repo.save(address);
    }

    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        log.debug("Fetching addresses for user ID: {}", userId);
        return repo.findByUserId(userId);
    }

    @Override
    public Address getAddressById(Long id) {
        log.debug("Fetching address by ID: {}", id);
        return repo.findById(id).orElseThrow(() -> {
            log.warn("Address ID {} not found.", id);
            return new RuntimeException("Address not found");
        });
    }

    @Override
    public void deleteAddress(Long id) {
        log.info("Deleting address ID: {}", id);
        repo.deleteById(id);
    }
}
