package com.microservice.item.repository;

import com.microservice.item.model.entity.Item;
import com.microservice.item.model.enums.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item,String> {

    List<Item> findByIdInOrderById(List<String> itemId);
    List<Item>findAllByIdInOrderById(List<String> itemIds);
    List<Item> findAllByItemStatus(ItemStatus itemStatus);
     Optional<Item> findByIdAndItemStatus(String id, ItemStatus itemStatus);
    Page<Item> findAllByItemStatus(ItemStatus status, Pageable pageable);

}
