package com.microservice.order.service;

import com.microservice.order.enums.CartStatus;
import com.microservice.order.enums.OrderStatus;
import com.microservice.order.error.RecordNotFoundExciption;
import com.microservice.order.model.dto.response.CartResponseDTO;
import com.microservice.order.model.dto.response.OrderResponseDTO;
import com.microservice.order.model.entity.Order;
import com.microservice.order.model.mapper.OrderMapper;
import com.microservice.order.proxy.CartProxy;
import com.microservice.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartProxy cartProxy;
    @Override
    public OrderResponseDTO createOrder(String userId, String cartId) {

        ResponseEntity<CartResponseDTO> cartResponse;
        try {
           cartResponse =  cartProxy.getCartById(cartId);
        }catch (Exception ex) {
            throw new RecordNotFoundExciption("Invalid cart ID : " + cartId + ". Cart not found");
        }
        CartResponseDTO cartDto = cartResponse.getBody();
        if(cartDto ==null ||  !cartDto.getUserId().equals(userId)){
            throw  new RecordNotFoundExciption("Invalid user ID" + userId + ". The cart does not belong to this user");
        }
        Optional<Order> existingOrders = orderRepository.findByCartIdAndCartStatus(cartId, CartStatus.ACTIVE);
        if (!existingOrders.isEmpty()) {
            throw new RecordNotFoundExciption("Order already exists for this cart");
        }
        Order order = orderMapper.toOrder(userId, cartId, cartDto.getItemList());
        Order orderSave = orderRepository.save(order);
        return orderMapper.toResponse(orderSave, cartDto.getItemList());
    }
    @Override
    public OrderResponseDTO updateOrder(String id, OrderStatus orderStatus) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundExciption("Order not found with ID: " + id));

        order.setOrderStatus(orderStatus);

        if (orderStatus == OrderStatus.DELETED) {
            order.setCartStatus(CartStatus.DELETED);
        }

        Order updatedOrder = orderRepository.save(order);

        CartResponseDTO cartResponse = fetchCartDetails(updatedOrder.getCartId());

        return orderMapper.toResponse(updatedOrder, cartResponse.getItemList());

    }
    @Override
    public OrderResponseDTO getOrderById(String id) {
        Order order = orderRepository.findByIdAndOrderStatus(id,OrderStatus.PENDING)
                .orElseThrow(() -> new RecordNotFoundExciption("Order not found"));
        CartResponseDTO cartResponse = fetchCartDetails(order.getCartId());
      return orderMapper.toResponse(order,cartResponse.getItemList());
    }
    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order>orders = orderRepository.findAllByOrderStatus(OrderStatus.PENDING);
        return orders.stream()
                .map(order -> {
                    CartResponseDTO cartResponse = fetchCartDetails(order.getCartId());
                    return orderMapper.toResponse(order,cartResponse.getItemList());
                }).collect(Collectors.toList());


    }
    @Override
    public OrderResponseDTO softDeleteOrder(String id) {
        Order order = orderRepository.findByIdAndOrderStatus(id, OrderStatus.PENDING)
                .orElseThrow(() -> new RecordNotFoundExciption("Order not found"));
        order.setOrderStatus(OrderStatus.DELETED);
        Order updateOrder = orderRepository.save(order);
        CartResponseDTO cartResponseDTO = fetchCartDetails(order.getCartId());
        return orderMapper.toResponse(updateOrder,cartResponseDTO.getItemList());
    }
    private CartResponseDTO fetchCartDetails(String cartId) {
        ResponseEntity<CartResponseDTO> response = cartProxy.getCartById(cartId);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RecordNotFoundExciption("Failed to fetch cart details for cart ID: " + cartId);
        }
        return response.getBody();
    }


}