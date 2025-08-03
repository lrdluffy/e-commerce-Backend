package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.Product;
import com.strawhats.ecommercebackend.model.Review;
import com.strawhats.ecommercebackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Optional<Review> findReviewByUserAndProduct(User user, Product product);

    Page<Review> findAllByProduct(Product product);
}
