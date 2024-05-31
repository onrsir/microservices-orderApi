package com.onursir.inventory_service.Repository;

import com.onursir.inventory_service.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String> {

    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
