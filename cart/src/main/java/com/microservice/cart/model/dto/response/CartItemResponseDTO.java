package com.microservice.cart.model.dto.response;

import com.microservice.cart.enums.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDTO {
    private String id;
    private String name;
    private Double price;
    private Integer quantity;

}
