package com.microservice.order.controller;

import com.microservice.order.enums.OrderStatus;
import com.microservice.order.model.dto.requset.OrderRequestDto;
import com.microservice.order.model.dto.response.OrderResponseDTO;
import com.microservice.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    public OrderService orderService;
    @PostMapping("/create-order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDto requestDto) {

       return ResponseEntity.ok(orderService.createOrder(requestDto.getUserId(), requestDto.getCartId()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable String id,
            @RequestParam OrderStatus orderStatus) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderStatus));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getCartById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    @GetMapping("get-All")
    public ResponseEntity<List<OrderResponseDTO>> getAllCarts() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    @DeleteMapping("/soft/{id}")
    public ResponseEntity<OrderResponseDTO>softDelete(@PathVariable String id){
        return ResponseEntity.ok(orderService.softDeleteOrder(id));
    }
    }