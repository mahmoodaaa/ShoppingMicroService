package com.microservice.cart.proxy;

import com.microservice.cart.model.dto.response.CartItemResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "Item-SERVICE"  ,path = "/item")
public interface ItemProxy {
    @GetMapping("item/{id}")
    public ResponseEntity<CartItemResponseDTO> getItemById(@PathVariable String id);
    @PostMapping("/fetch-items")
    public ResponseEntity<List<CartItemResponseDTO>> getItemsByIds(@RequestBody  List<String> itemIds);
    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> getAllItems();
}