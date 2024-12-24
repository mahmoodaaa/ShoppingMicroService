package com.microservice.item.model.dto;

import com.microservice.item.model.enums.ItemStatus;
import com.microservice.item.model.enums.ItemType;
import com.microservice.item.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDTO {
    @NotNull
    private String name;
    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    private String description;
    @ValidEnum(value = ItemStatus.class, message = "Invalid Item-Status.")
    private ItemStatus itemStatus ;
    @ValidEnum(value = ItemType.class, message = "Invalid Type Item.")
    private ItemType itemType;
}
