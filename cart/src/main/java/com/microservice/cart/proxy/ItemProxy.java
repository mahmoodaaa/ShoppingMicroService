package com.microservice.cart.proxy;

import com.microservice.cart.model.dto.response.CartItemResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ITEM-SERVICE"  ,path = "/api/items")
public interface ItemProxy {
    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> getItemById(@PathVariable String id);
    @PostMapping("/fetch-items")
    public ResponseEntity<List<CartItemResponseDTO>> getItemsByIds(@RequestBody  List<String> itemIds);
    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> getAllItems();
}