package com.microservice.cart.model.dto.response;

import com.microservice.cart.enums.CartStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {

    private String id;
    private String userId;
    @Valid
    private List<CartItemResponseDTO> itemList;
    private CartStatus cartStatus;
}
