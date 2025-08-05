package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.config.AppConstants;
import com.strawhats.ecommercebackend.payload.CategoryDTO;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "JWT Authorization")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Category already exists")
    })
    @Operation(summary = "Create Category", description = "Create a new category")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category object to be created", required = true)
            @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryDTO createdCategoryDTO = categoryService.createCategory(categoryDTO);
        return  new ResponseEntity<>(createdCategoryDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @Operation(summary = "Get Categories", description = "Get list of categories")
    @GetMapping("/public/categories")
    public ResponseEntity<PagedResponse<CategoryDTO>> getCategories(
        @Parameter(name = "Page Number", required = false)
        @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
        @Parameter(name = "Page Size", required = false)
        @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
        @Parameter(name = "Sort Field", required = false)
        @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.CATEGORY_SORT_FIELD) String sortField,
        @Parameter(name = "Sort Direction", required = false)
        @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<CategoryDTO> categoryDTOPagedResponse = categoryService.getCategories(pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(categoryDTOPagedResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Category Not Found")
    })
    @Operation(summary = "Update Category", description = "Update an existing category")
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Parameter(name = "categoryId", required = true)
            @PathVariable Long categoryId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category object to be replaced", required = true)
            @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Category Not Found")
    })
    @Operation(summary = "Delete Category", description = "Delete a category")
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(
            @Parameter(name = "categoryId", required = true)
            @PathVariable Long categoryId
    ) {
        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }
}
