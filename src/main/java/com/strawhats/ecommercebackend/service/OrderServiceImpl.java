package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.*;
import com.strawhats.ecommercebackend.payload.OrderDTO;
import com.strawhats.ecommercebackend.payload.OrderItemDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.repository.AddressRepository;
import com.strawhats.ecommercebackend.repository.OrderItemRepository;
import com.strawhats.ecommercebackend.repository.OrderRepository;
import com.strawhats.ecommercebackend.repository.ProductRepository;
import com.strawhats.ecommercebackend.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = authUtils.loggedInUser();
        Address address = addressRepository.findAddressByUserAndAddressId(user, orderDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", orderDTO.getAddressId()));

        Cart userCart = cartService.deleteCart(user.getCart().getCartId());

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setTotalPrice(userCart.getTotalPrice());

        List<OrderItem> orderItems = userCart.getCartItems()
                .stream()
                .map(cartItem -> {
                    OrderItem orderItem = modelMapper.map(cartItem, OrderItem.class);
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList();
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemDTO> orderItemDTOS = savedOrder.getOrderItems()
                .stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setProductId(orderItem.getProduct().getProductId());
                    orderItemDTO.setQuantity(orderItem.getQuantity());
                    return orderItemDTO;
                }).toList();

        OrderDTO savedOrderDTO = new OrderDTO();
        savedOrderDTO.setItems(orderItemDTOS);
        savedOrderDTO.setAddressId(savedOrder.getAddress().getAddressId());

        return savedOrderDTO;
    }

    @Override
    public PagedResponse<OrderDTO> getOrders(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        User user = authUtils.loggedInUser();
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Order> orderPage = orderRepository.findAllByUser(user, pageable);

        List<OrderDTO> content = orderPage.stream()
                .map(order -> {
                    List<OrderItemDTO> orderItemDTOS = order.getOrderItems()
                            .stream()
                            .map(orderItem -> {
                                OrderItemDTO orderItemDTO = new OrderItemDTO();
                                orderItemDTO.setProductId(orderItem.getProduct().getProductId());
                                orderItemDTO.setQuantity(orderItem.getQuantity());
                                return orderItemDTO;
                            }).toList();

                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setItems(orderItemDTOS);
                    orderDTO.setAddressId(order.getAddress().getAddressId());
                    return orderDTO;
                }).toList();

        PagedResponse<OrderDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(orderPage.getNumber());
        pagedResponse.setPageSize(orderPage.getSize());
        pagedResponse.setTotalPages(orderPage.getTotalPages());
        pagedResponse.setTotalElements(orderPage.getTotalElements());
        pagedResponse.setIsLastPage(orderPage.isLast());

        return pagedResponse;
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        User user = authUtils.loggedInUser();
        Order order = orderRepository.findOrderByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        List<OrderItemDTO> orderItemDTOS = order.getOrderItems()
                .stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setProductId(orderItem.getProduct().getProductId());
                    orderItemDTO.setQuantity(orderItem.getQuantity());
                    return orderItemDTO;
                }).toList();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setItems(orderItemDTOS);
        orderDTO.setAddressId(order.getAddress().getAddressId());

        return orderDTO;
    }
}
