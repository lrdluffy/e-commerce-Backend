package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.config.AppConstants;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.payload.ReviewDTO;
import com.strawhats.ecommercebackend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review Module", description = "APIs & Rest Endpoints related to Review Module")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Product Not Found"),
            @ApiResponse(responseCode = "400", description = "Review form the user to this product already exists")
    })
    @Operation(summary = "Add Review", description = "Add product review")
    @PostMapping("/user/products/{productId}/review")
    public ResponseEntity<ReviewDTO> addReview(
            @Parameter(name = "productId", required = true)
            @PathVariable Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Review object to be added", required = true)
            @RequestBody ReviewDTO reviewDTO
    ) {
        ReviewDTO addedReviewDTO = reviewService.addReview(productId, reviewDTO);
        return new ResponseEntity<>(addedReviewDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @Operation(summary = "Get Reviews", description = "List product reviews")
    @GetMapping("/public/products/{productId}/reviews")
    public ResponseEntity<PagedResponse<ReviewDTO>> getReviews(
            @Parameter(name = "productId", required = true)
            @PathVariable Long productId,
            @Parameter(name = "Page Number", required = false)
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @Parameter(name = "Page Size", required = false)
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @Parameter(name = "Sort Field", required = false)
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.REVIEW_SORT_FIELD) String sortField,
            @Parameter(name = "Sort Direction", required = false)
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<ReviewDTO> reviewDTOPagedResponse = reviewService.getReviews(productId, pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(reviewDTOPagedResponse, HttpStatus.OK);
    }
}
