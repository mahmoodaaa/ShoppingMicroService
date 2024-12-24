package com.microservice.item.model.dto;

import com.microservice.item.model.enums.ItemStatus;
import com.microservice.item.model.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDTO {

    private String id;
    private String name;
    private Double price;
    private String description;
    private ItemStatus itemStatus ;
    private ItemType itemType;
}
