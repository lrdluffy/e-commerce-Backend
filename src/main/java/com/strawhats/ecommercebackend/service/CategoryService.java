package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.payload.CategoryDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    PagedResponse<CategoryDTO> getCategories(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);
}
