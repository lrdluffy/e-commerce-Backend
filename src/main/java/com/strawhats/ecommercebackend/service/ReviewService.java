package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.payload.ReviewDTO;

public interface ReviewService {

    ReviewDTO addReview(Long productId, ReviewDTO reviewDTO);

    PagedResponse<ReviewDTO>  getReviews(Long productId, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
}
