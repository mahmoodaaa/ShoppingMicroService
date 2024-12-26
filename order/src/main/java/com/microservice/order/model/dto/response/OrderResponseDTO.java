package com.microservice.order.model.dto.response;

import com.microservice.order.enums.CartStatus;
import com.microservice.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private String id;
    private String userId;
    private  String cartId;
    private Double totalAmount;
    private String reference;
    private OrderStatus orderStatus ;
    private CartStatus cartStatus;
    private LocalDateTime createdAt;
    List<CartItemResponseDTO>cartItems;
}
