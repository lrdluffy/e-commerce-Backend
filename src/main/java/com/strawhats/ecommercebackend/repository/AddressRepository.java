package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.Address;
import com.strawhats.ecommercebackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findAddressByUserAndPostalCode(User user, String postalCode);

    Page<Address> findAddressesByUser(User user, Pageable pageable);

    Optional<Address> findAddressesByUserAndAddressId(User user, Long addressId);
}
