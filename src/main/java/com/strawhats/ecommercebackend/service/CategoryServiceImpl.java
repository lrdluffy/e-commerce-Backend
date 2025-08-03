package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ApiException;
import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.Category;
import com.strawhats.ecommercebackend.payload.CategoryDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.repository.CategoryRepository;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryRepository.findCategoryByCategoryName(categoryDTO.getCategoryName())
                .ifPresent(
                        category -> new ApiException("There is already a category with name " + category.getCategoryName())
                );

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);

        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return savedCategoryDTO;
    }

    @Override
    public PagedResponse<CategoryDTO> getCategories(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDTO> content = categoryPage.getContent()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        PagedResponse<CategoryDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(pageNumber);
        pagedResponse.setPageSize(pageSize);
        pagedResponse.setTotalPages(categoryPage.getTotalPages());
        pagedResponse.setTotalElements(categoryPage.getTotalElements());
        pagedResponse.setIsLastPage(categoryPage.isLast());

        return pagedResponse;
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Category category = categoryRepository.findCategoryByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category",  "categoryId", categoryId));

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findCategoryByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category",  "categoryId", categoryId));

        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        newCategory.setCategoryId(category.getCategoryId());

        Category updatedCategory = categoryRepository.save(newCategory);
        CategoryDTO updatedCategoryDTO = modelMapper.map(updatedCategory, CategoryDTO.class);

        return updatedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findCategoryByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category",  "categoryId", categoryId));

        categoryRepository.delete(category);
        CategoryDTO deletedCategoryDTO = modelMapper.map(category, CategoryDTO.class);

        return deletedCategoryDTO;
    }
}
