package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ApiException;
import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.Product;
import com.strawhats.ecommercebackend.model.Review;
import com.strawhats.ecommercebackend.model.User;
import com.strawhats.ecommercebackend.payload.PagedResponse;
import com.strawhats.ecommercebackend.payload.ReviewDTO;
import com.strawhats.ecommercebackend.repository.ProductRepository;
import com.strawhats.ecommercebackend.repository.ReviewRepository;
import com.strawhats.ecommercebackend.utils.AuthUtils;
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
public class ReviewServiceImpl implements ReviewService {

    private final AuthUtils authUtils;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDTO addReview(Long productId, ReviewDTO reviewDTO) {
        User user = authUtils.loggedInUser();
        Product product = productRepository.findProductByProductId(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        reviewRepository.findReviewByUserAndProduct(user, product)
                .ifPresent(review -> new ApiException("You have already submitted a review for this product."));

        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setUser(user);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        ReviewDTO savedReviewDTO = modelMapper.map(savedReview, ReviewDTO.class);
        savedReviewDTO.setUserId(savedReview.getUser().getUserId());
        savedReviewDTO.setProductId(savedReview.getProduct().getProductId());

        return savedReviewDTO;
    }

    @Override
    public PagedResponse<ReviewDTO> getReviews(Long productId, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Product product = productRepository.findProductByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Review> reviewPage = reviewRepository.findAllByProduct(product, pageable);

        List<ReviewDTO> content = reviewPage.getContent()
                .stream()
                .map(review -> {
                    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
                    reviewDTO.setUserId(review.getUser().getUserId());
                    reviewDTO.setProductId(review.getProduct().getProductId());
                    return reviewDTO;
                }).toList();

        PagedResponse<ReviewDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(reviewPage.getNumber());
        pagedResponse.setPageSize(reviewPage.getSize());
        pagedResponse.setTotalPages(reviewPage.getTotalPages());
        pagedResponse.setTotalElements(reviewPage.getTotalElements());
        pagedResponse.setIsLastPage(reviewPage.isLast());

        return pagedResponse;
    }
}
