package com.microservice.order.model.mapper;

import com.microservice.order.enums.CartStatus;
import com.microservice.order.enums.OrderStatus;
import com.microservice.order.model.dto.response.CartItemResponseDTO;
import com.microservice.order.model.dto.response.CartResponseDTO;
import com.microservice.order.model.dto.response.OrderResponseDTO;
import com.microservice.order.model.entity.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class OrderMapper {
    public Order toOrder(String userId, String cartId, List<CartItemResponseDTO> cartItems) {

            Double totalAmount = cartItems.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            return Order.builder()
                    .userId(userId)
                    .cartId(cartId)
                    .totalAmount(totalAmount)
                    .reference(UUID.randomUUID().toString())
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        public OrderResponseDTO toResponse(Order order, List<CartItemResponseDTO> cartItems) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .cartId(order.getCartId())
                .totalAmount(order.getTotalAmount())
                .reference(order.getReference())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .cartItems(cartItems)// Enriched cart items
                .cartStatus(CartStatus.ACTIVE)
                .build();
    }
}

