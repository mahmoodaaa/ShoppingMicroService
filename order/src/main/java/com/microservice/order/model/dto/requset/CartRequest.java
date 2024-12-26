package com.microservice.order.model.dto.requset;

import com.microservice.order.enums.CartStatus;
import com.microservice.order.validation.ValidEnum;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CartRequest {
    @NotNull
    private String userId;
    private List<CartItemReq> itemList = new ArrayList<>();
    @ValidEnum(value = CartStatus.class, message = "Invalid Cart-Status.")
    private CartStatus cartStatus;
}
