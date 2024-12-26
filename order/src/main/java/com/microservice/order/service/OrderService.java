package com.microservice.order.service;

import com.microservice.order.enums.OrderStatus;
import com.microservice.order.model.dto.response.CartResponseDTO;
import com.microservice.order.model.dto.response.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(String userId, String cartId);
    OrderResponseDTO updateOrder(String id, OrderStatus orderStatus);
    OrderResponseDTO getOrderById(String id);
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO softDeleteOrder(String id);

}
