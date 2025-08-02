package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ApiException;
import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.Address;
import com.strawhats.ecommercebackend.model.User;
import com.strawhats.ecommercebackend.payload.AddressDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.repository.AddressRepository;
import com.strawhats.ecommercebackend.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AuthUtils authUtils;
    private final ModelMapper modelMapper;

    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        User user = authUtils.loggedInUser();
        Optional<Address> addressOptional = addressRepository.findAddressByUserAndPostalCode(user, addressDTO.getPostalCode());

        if (addressOptional.isPresent()) {
            throw new ApiException("Address with postal code " + addressDTO.getPostalCode() + " already exists");
        }

        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        AddressDTO savedAddressDTO = modelMapper.map(savedAddress, AddressDTO.class);

        return savedAddressDTO;
    }

    @Override
    public PagedResponse<AddressDTO> getAddresses(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        User user = authUtils.loggedInUser();

        Page<Address> addressPage =  addressRepository.findAddressesByUser(user, pageable);

        List<AddressDTO> content = addressPage.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

        PagedResponse<AddressDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(addressPage.getNumber());
        pagedResponse.setPageSize(addressPage.getSize());
        pagedResponse.setTotalPages(addressPage.getTotalPages());
        pagedResponse.setTotalElements(addressPage.getTotalElements());
        pagedResponse.setIsLastPage(addressPage.isLast());

        return pagedResponse;
    }

    @Override
    public AddressDTO deleteAddress(Long addressId) {
        User user = authUtils.loggedInUser();
        Address address = addressRepository.findAddressesByUserAndAddressId(user, addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressRepository.delete(address);

        AddressDTO deletedAddressDTO = modelMapper.map(address, AddressDTO.class);
        return deletedAddressDTO;
    }
}
