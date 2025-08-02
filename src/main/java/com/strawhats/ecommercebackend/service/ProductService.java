package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.payload.ProductDTO;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    PagedResponse<ProductDTO> getProducts(Integer pagdNumber, Integer pageSize, String sortField, String sortDirection);

    ProductDTO getProductById(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);
}
