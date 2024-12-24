package com.microservice.item.controller;

import com.microservice.item.model.dto.ItemRequestDTO;
import com.microservice.item.model.dto.ItemResponseDTO;
import com.microservice.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @PostMapping("/add-item")
    public ResponseEntity<ItemResponseDTO> createItem( @Valid @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.ok(itemService.createItem(dto));
    }
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {

        return ResponseEntity.ok(itemService.getAllItems());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }
    @PostMapping("/fetch-items")
    public ResponseEntity<List<ItemResponseDTO>> getItemsByIds(@RequestBody  List<String> itemIds) {
           return ResponseEntity.ok(itemService.getItemsByIds(itemIds));
    }
    @PutMapping("update/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable String id, @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.ok(itemService.updateItem(id, dto));
    }
    @DeleteMapping("soft/{id}")
    public ResponseEntity<Void> softDeleteItem(@PathVariable String id) {
        itemService.softDeleteItem(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("hard/{id}")
    public ResponseEntity<Void> hardDeleteItem(@PathVariable String id) {
        itemService.hardDeleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
