package com.microservice.cart.model.mapper;

import com.microservice.cart.enums.CartStatus;
import com.microservice.cart.error.RecordNotFoundExciption;
import com.microservice.cart.model.dto.requset.CartItemReq;
import com.microservice.cart.model.dto.response.CartItemResponseDTO;
import com.microservice.cart.model.dto.requset.CartReqDTO;
import com.microservice.cart.model.dto.response.CartResponseDTO;
import com.microservice.cart.model.entity.Cart;
import com.microservice.cart.model.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {


    // Convert CartReqDTO to Cart entity
    public Cart toEntity(CartReqDTO cartReqDTO) {
        return Cart.builder()

                .userId(cartReqDTO.getUserId())
                .itemList(cartReqDTO.getItemList().stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList()))
                .cartStatus(CartStatus.ACTIVE)
                .build();
    }

    // Convert CartItemReq to CartItem entity
    public CartItem toEntity(CartItemReq cartItemReq) {
        return CartItem.builder()
                .id(cartItemReq.getId())
                .quantity(cartItemReq.getQuantity())
                .build();
    }

    // Convert Cart entity to CartResponseDTO
    public CartResponseDTO toResponse(Cart cart) {
        return CartResponseDTO.builder()
                .id(cart.getId()) // Map cart ID
                .userId(cart.getUserId()) // Map user ID
                .cartStatus(cart.getCartStatus()) // Map cart status
                .itemList(cart.getItemList().stream() // Map each item in the cart
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Convert CartItem entity to CartItemResponseDTO
    public CartItemResponseDTO toResponse(CartItem cartItem) {
        return CartItemResponseDTO.builder()
                .id(cartItem.getId()) // Map item ID
                .quantity(cartItem.getQuantity()) // Map quantity
                .build();
    }

    // Convert CartItem entity to CartItemResponseDTO
    public CartResponseDTO toResponse(Cart cart, List<CartItemResponseDTO> itemDetails) {
        List<CartItemResponseDTO> responseItems = cart.getItemList().stream()
                .map(cartItem -> {
                    CartItemResponseDTO itemDetail = itemDetails.stream()
                            .filter(detail -> detail.getId().equals(cartItem.getId()))
                            .findFirst()
                            .orElseThrow(() -> new RecordNotFoundExciption("Item not found"));
                    return toResponse(cartItem, itemDetail);
                })
                .collect(Collectors.toList());

        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .cartStatus(cart.getCartStatus())
                .itemList(responseItems)
                .build();
    }

    // Convert CartItemDetailsDTO to CartItemResponseDTO
    public CartItemResponseDTO toResponse(CartItem cartItem, CartItemResponseDTO itemDetails) {
        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .name(itemDetails.getName())
                .price(itemDetails.getPrice())
                .quantity(cartItem.getQuantity()) // Use quantity from CartItem
                .build();
    }








}
