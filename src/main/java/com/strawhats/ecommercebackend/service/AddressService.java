package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.payload.AddressDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;

public interface AddressService {

    AddressDTO addAddress(AddressDTO addressDTO);

    PagedResponse<AddressDTO> getAddresses(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    AddressDTO deleteAddress(Long addressId);
}
