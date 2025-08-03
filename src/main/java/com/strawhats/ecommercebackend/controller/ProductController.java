package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.config.AppConstants;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.payload.ProductDTO;
import com.strawhats.ecommercebackend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "\uD83D\uDED2 Product Module", description = "APIs & Rest Endpoints related to products")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class ProductController {

    private final ProductService productService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Product Already Exists")
    })
    @Operation(summary = "Create Product", description = "Create a new product")
    @PostMapping("/admin/products")
    public ResponseEntity<ProductDTO> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product object to be created")
            @RequestBody ProductDTO productDTO) {
        ProductDTO createdProductDTO =  productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @Operation(summary = "Get Products", description = "Get a list of products")
    @GetMapping("/public/products")
    public ResponseEntity<PagedResponse<ProductDTO>> getProducts(
            @Parameter(name = "Page Number", required = false)
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @Parameter(name = "Page Size", required = false)
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @Parameter(name = "Sort Field", required = false)
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.PRODUCT_SORT_FIELD) String sortField,
            @Parameter(name = "Sort Direction",  required = false)
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<ProductDTO> productDTOPagedResponse = productService.getProducts(pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(productDTOPagedResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Product Not Found")
    })
    @Operation(summary = "Get Product By Id", description = "Get a single product by ID")
    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(name = "productId", required = true)
            @PathVariable Long productId
    ) {
        ProductDTO productDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Product Not Found")
    })
    @Operation(summary = "Update Product", description = "Update an existing product")
    @PutMapping("/admin/products/{productId}")
    private ResponseEntity<ProductDTO> updateProduct(
            @Parameter(name = "productId", required = true)
            @PathVariable Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product object to be replaced", required = true)
            @RequestBody ProductDTO productDTO
    ){
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Product Not Found")
    })
    @Operation(summary = "Delete Product", description = "Delete a product")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(
            @Parameter(name = "productId", required = true)
            @PathVariable Long productId
    ) {
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
