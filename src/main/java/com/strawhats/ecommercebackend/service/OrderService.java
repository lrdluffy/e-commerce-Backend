package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.payload.OrderDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);

    PagedResponse<OrderDTO> getOrders(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    OrderDTO getOrderById(Long orderId);
}
