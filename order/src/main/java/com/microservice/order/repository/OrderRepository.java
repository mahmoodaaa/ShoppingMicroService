package com.microservice.order.repository;

import com.microservice.order.enums.CartStatus;
import com.microservice.order.enums.OrderStatus;
import com.microservice.order.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
    Optional<Order> findByIdAndOrderStatus(String id,OrderStatus orderStatus);
    List<Order> findByCartId(String cartId);
    Optional<Order> findByCartIdAndCartStatus(String id, CartStatus cartStatus);

}
