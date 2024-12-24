package com.microservice.item.service;

import com.microservice.item.error.RecordNotFoundExciption;
import com.microservice.item.model.dto.ItemRequestDTO;
import com.microservice.item.model.dto.ItemResponseDTO;
import com.microservice.item.model.entity.Item;
import com.microservice.item.model.enums.ItemStatus;
import com.microservice.item.model.mapper.ItemMapper;
import com.microservice.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
  @Autowired
    private ItemRepository itemRepository;
  @Autowired
    private ItemMapper itemMapper;
    public List<ItemResponseDTO> getItemsByIds(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new RecordNotFoundExciption("Item IDs list cannot be null or empty");
        }
        // Fetch all items by IDs where isDeleted is false
        List<Item> items = itemRepository.findByIdInOrderById(itemIds);

        if (items.isEmpty()) {
            throw new RecordNotFoundExciption("No items found for the provided IDs");
        }
        // Map the list of items to a list of response DTOs
        return items.stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }
    public ItemResponseDTO createItem(ItemRequestDTO dto) {
          Item item = itemMapper.toEntity(dto);
          Item save = itemRepository.save(item);
           return itemMapper.toResponse(save);
     }
     public List<ItemResponseDTO> getAllItems() {

            return itemRepository.findAllByItemStatus(ItemStatus.ACTIVE)
                    .stream()
                    .map(itemMapper::toResponse)
                    .toList();
    }
    public ItemResponseDTO getItemById( String id) {

           return itemRepository.findByIdAndItemStatus(id,ItemStatus.ACTIVE)
                .map(itemMapper::toResponse)
                .orElseThrow(()-> new RecordNotFoundExciption("item not found"));
        }

    public ItemResponseDTO updateItem(String id, ItemRequestDTO dto)     {

        try {
            Item existingItem = itemRepository.findByIdAndItemStatus(id,ItemStatus.ACTIVE)
                    .orElseThrow(() -> new RecordNotFoundExciption("Item not found"));
            existingItem.setName(dto.getName());
            existingItem.setPrice(dto.getPrice());
            existingItem.setDescription(dto.getDescription());
            existingItem.setItemType(dto.getItemType());

            return itemMapper.toResponse(itemRepository.save(existingItem));
        } catch (NoSuchElementException ex) {
        throw new RecordNotFoundExciption(String.format("no found with Item was found in database"));
    }

    }
    public void softDeleteItem(String id) {
        Item item = itemRepository.findByIdAndItemStatus(id,ItemStatus.ACTIVE)
                .orElseThrow(() -> new RecordNotFoundExciption("Item id not found"));
        item.setItemStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }
    public void hardDeleteItem(String id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Page<ItemResponseDTO> getAllItemPaginated(int page,int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Item>itemPage = itemRepository.findAllByItemStatus(ItemStatus.ACTIVE,pageable);
        return itemPage.map(itemMapper::toResponse);
    }


}









