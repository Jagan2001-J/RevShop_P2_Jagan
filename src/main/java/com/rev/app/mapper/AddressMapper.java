package com.rev.app.mapper;

import com.rev.app.dto.AddressRequestDTO;
import com.rev.app.dto.AddressResponseDTO;
import com.rev.app.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Address.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .country(dto.getCountry())
                .build();
    }

    public AddressResponseDTO toDto(Address entity) {
        if (entity == null) {
            return null;
        }

        return AddressResponseDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .country(entity.getCountry())
                .build();
    }
}
