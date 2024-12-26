package com.microservice.cart.model.dto.requset;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartReqDTO {


    private String cartItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
