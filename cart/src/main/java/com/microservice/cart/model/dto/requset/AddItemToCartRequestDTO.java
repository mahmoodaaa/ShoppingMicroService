package com.microservice.cart.model.dto.requset;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequestDTO {
    @NotNull
    private String userId;
    @NotNull
    private String cartItemId;

}
