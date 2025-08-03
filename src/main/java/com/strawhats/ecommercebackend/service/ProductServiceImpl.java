package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ApiException;
import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.Category;
import com.strawhats.ecommercebackend.model.Product;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.payload.ProductDTO;
import com.strawhats.ecommercebackend.repository.CategoryRepository;
import com.strawhats.ecommercebackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findCategoryByCategoryId(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category",  "categoryId", productDTO.getCategoryId()));

        productRepository.findProductByCategoryAndProductName(category, productDTO.getProductName())
                .ifPresent(product -> new ApiException("Product with product name " + productDTO.getProductName() + " already exists in this category!"));

        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
        savedProductDTO.setCategoryId(savedProduct.getCategory().getCategoryId());

        return savedProductDTO;
    }

    @Override
    public PagedResponse<ProductDTO> getProducts(Integer pagdNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pagdNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> content = productPage.getContent()
                .stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setCategoryId(product.getCategory().getCategoryId());
                    return productDTO;
                }).toList();

        PagedResponse<ProductDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(productPage.getNumber());
        pagedResponse.setPageSize(productPage.getSize());
        pagedResponse.setTotalPages(productPage.getTotalPages());
        pagedResponse.setTotalElements(productPage.getTotalElements());
        pagedResponse.setIsLastPage(productPage.isLast());

        return pagedResponse;
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findProductByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",  "productId", productId));

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setCategoryId(product.getCategory().getCategoryId());

        return productDTO;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Category category = categoryRepository.findCategoryByCategoryId(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category",  "categoryId", productDTO.getCategoryId()));

        Product product = productRepository.findProductByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",  "productId", productId));

        Product newProduct = modelMapper.map(productDTO, Product.class);
        newProduct.setCategory(category);
        Product updatedProduct = productRepository.save(newProduct);

        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
        updatedProductDTO.setCategoryId(updatedProduct.getCategory().getCategoryId());
        return updatedProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findProductByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",  "productId", productId));

        productRepository.delete(product);

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setCategoryId(product.getCategory().getCategoryId());

        return productDTO;}
}
