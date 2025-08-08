package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.config.AppConstants;
import com.strawhats.ecommercebackend.payload.OrderDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "\uD83D\uDED2 Order Management", description = "APIs & Rest Endpoints related to Order Management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class OrderController {

    private final OrderService orderService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Address or Product Not Found")
    })
    @Operation(summary = "Create Order", description = "Place an order")
    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO orderDTO
    ) {
        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrderDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @Operation(summary = "Get Orders", description = "List user orders")
    @GetMapping("/orders")
    public ResponseEntity<PagedResponse<OrderDTO>> getOrders(
            @Parameter(name = "Page Number", required = false)
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @Parameter(name = "Page Size", required = false)
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @Parameter(name = "Sort Field", required = false)
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.ORDER_SORT_FIELD) String sortField,
            @Parameter(name = "Sort Direction", required = false)
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<OrderDTO> orderDTOPagedResponse = orderService.getOrders(pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(orderDTOPagedResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Order Not Found")
    })
    @Operation(summary = "Get Order Detail", description = "Retrieve order details")
    @GetMapping("orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(
            @Parameter(name = "orderId", required = true)
            @PathVariable Long orderId
    ) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
