package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.Order;
import com.strawhats.ecommercebackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findAllByUser(User user, Pageable pageable);

    Optional<Order> findOrderByOrderIdAndUser(Long orderId, User user);
}
