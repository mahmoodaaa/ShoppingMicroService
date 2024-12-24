package com.microservice.cart.repository;

import com.microservice.cart.enums.CartStatus;
import com.microservice.cart.model.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart,String> {
    List<Cart> findAllByCartStatus(CartStatus cartStatus);
    Page<Cart> findAllByCartStatus(CartStatus status, Pageable pageable);
    Optional<Cart> findByIdAndCartStatus(String id, CartStatus cartStatus);
    List<Cart> findByUserIdAndCartStatus(String userId, CartStatus cartStatus);



}
