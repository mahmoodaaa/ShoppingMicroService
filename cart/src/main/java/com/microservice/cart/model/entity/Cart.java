package com.microservice.cart.model.entity;

import com.microservice.cart.enums.CartStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List< CartItem> itemList = new ArrayList<>();
    private CartStatus cartStatus = CartStatus.ACTIVE;
}