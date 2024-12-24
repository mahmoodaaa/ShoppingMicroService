package com.microservice.cart.model.dto.requset;

import com.microservice.cart.enums.CartStatus;
import com.microservice.cart.validation.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartReqDTO {
   @NotNull
    private String userId;
    private List<CartItemReq> itemList = new ArrayList<>();
    @ValidEnum(value = CartStatus.class, message = "Invalid Cart-Status.")
    private CartStatus cartStatus;


}
