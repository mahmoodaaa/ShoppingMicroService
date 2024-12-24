package com.microservice.item.model.mapper;

import com.microservice.item.model.dto.ItemRequestDTO;
import com.microservice.item.model.dto.ItemResponseDTO;
import com.microservice.item.model.entity.Item;
import com.microservice.item.model.enums.ItemStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public Item toEntity(ItemRequestDTO itemDTO) {

        return Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .description(itemDTO.getDescription())
                .itemStatus(ItemStatus.ACTIVE)
                .itemType(itemDTO.getItemType())
                .build();
    }
    public ItemResponseDTO toResponse(Item item) {
        return ItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .description(item.getDescription())
                .itemStatus(ItemStatus.ACTIVE)
                .itemType(item.getItemType())
                .build();
    }
   public List<ItemResponseDTO> toItemResponseList(List<Item> items) {
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public List<Item> toEntityList(List<ItemRequestDTO> itemRequestDTOs) {
        return itemRequestDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }


}

