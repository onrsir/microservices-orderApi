package com.onursir.inventory_service;

import com.onursir.inventory_service.Repository.InventoryRepository;
import com.onursir.inventory_service.model.Inventory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			inventoryRepository.save(new Inventory("1", "123", 100));
			inventoryRepository.save(new Inventory("2", "456", 200));
		};

	}
}