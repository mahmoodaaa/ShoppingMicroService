package com.microservice.cart.controller;

import com.microservice.cart.model.dto.requset.AddItemToCartRequestDTO;
import com.microservice.cart.model.dto.requset.CartItemReq;
import com.microservice.cart.model.dto.requset.CartReqDTO;
import com.microservice.cart.model.dto.requset.UpdateCartReqDTO;
import com.microservice.cart.model.dto.response.CartResponseDTO;
import com.microservice.cart.model.entity.CartItem;
import com.microservice.cart.service.CartServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private CartServiceImpl cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<CartResponseDTO> createCart(String userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(userId));
    }
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> createCartToItem(@PathVariable String userId,@RequestBody List<CartItemReq> items) {
        List<CartItem> cartItems = items.stream()
                .map(req -> new CartItem(req.getId(), req.getQuantity()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.createCartToItem(userId, cartItems));

    }
    @PutMapping("/add-itemsToCart")
    public ResponseEntity<CartResponseDTO> addItemsToCart(@RequestBody AddItemToCartRequestDTO add) {
        return ResponseEntity.ok(cartService.addItemToCart(add.getUserId(), add.getCartItemId()));
    }

    @PutMapping("/remove-item")
    public ResponseEntity<CartResponseDTO> removeItemToCart(@RequestBody AddItemToCartRequestDTO add){
        return ResponseEntity.ok(cartService.removeItemToCart(add.getUserId(), add.getCartItemId() ));
    }

    @PutMapping("/{cartId}/update")
    public ResponseEntity<CartResponseDTO> update(@PathVariable String cartId,
                                                  @RequestBody @Valid List<UpdateCartReqDTO> dto){
        return ResponseEntity.ok(cartService.update(cartId,dto));
    }
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }
   @GetMapping
   public ResponseEntity<List<CartResponseDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }
    @DeleteMapping("hard/{cartId}")
    public  ResponseEntity<Void>hardDeleteCart(@PathVariable String cartId) {
         cartService.hardDeleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/soft/{cartId}")
    public ResponseEntity<CartResponseDTO>softDelete(@PathVariable String cartId){
         return ResponseEntity.ok(cartService.softDeleteCart(cartId));
    }
    @GetMapping("/paginated")
    public ResponseEntity<Page<CartResponseDTO>> getAllCartsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(cartService.getAllCartsPaginated(page,size));
    }


}
