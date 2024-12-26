package com.microservice.order.model.entity;

import com.microservice.order.enums.CartStatus;
import com.microservice.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "order-cart")

public class Order {
    private String id;
    private String userId;
    private  String cartId;
    private Double totalAmount;
    private String reference;
    private OrderStatus orderStatus ;
    private CartStatus cartStatus;
    private LocalDateTime createdAt;
}
