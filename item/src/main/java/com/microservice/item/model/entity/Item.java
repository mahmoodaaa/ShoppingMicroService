package com.microservice.item.model.entity;

import com.microservice.item.model.enums.ItemStatus;
import com.microservice.item.model.enums.ItemType;
import com.microservice.item.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "item")
public class Item {
    @Id
    private String id;
    private String name;
    private Double price;
    private String description;
    private ItemStatus itemStatus =ItemStatus.ACTIVE;
    private ItemType itemType;

}
