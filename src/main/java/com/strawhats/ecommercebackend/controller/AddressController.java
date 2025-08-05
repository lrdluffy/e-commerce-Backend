package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.config.AppConstants;
import com.strawhats.ecommercebackend.payload.AddressDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address Module", description = "APIs & Rest Endpoints related to Address Module")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/addresses")
public class AddressController {

    private final AddressService addressService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Address already exists")
    })
    @Operation(summary = "Add Address", description = "Add new addresses")
    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Address object to be added", required = true)
            @RequestBody AddressDTO addressDTO
    ) {
        AddressDTO addedAddressDTO = addressService.addAddress(addressDTO);
        return new ResponseEntity<>(addedAddressDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @Operation(summary = "Get Addresses", description = "List user addresses")
    @GetMapping
    public ResponseEntity<PagedResponse<AddressDTO>> getAddresses(
            @Parameter(name = "Page Number", required = false)
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @Parameter(name = "Page Size", required = false)
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @Parameter(name = "Sort Field", required = false)
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.ADDRESS_SORT_FIELD) String sortField,
            @Parameter(name = "Sort Direction", required = false)
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<AddressDTO> addressDTOPagedResponse = addressService.getAddresses(pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(addressDTOPagedResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Address Not Found")
    })
    @Operation(summary = "Delete Address", description = "Delete address")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddress(
            @Parameter(name = "addressId", required = true)
            @PathVariable Long addressId
    ) {
        AddressDTO deleteAddressDTO = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(deleteAddressDTO, HttpStatus.OK);
    }

}
