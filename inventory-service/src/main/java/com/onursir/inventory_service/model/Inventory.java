package com.onursir.inventory_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "inventory")
public class Inventory {
    @Id
    private String id;
    private String skuCode;
    private Integer quantity;
}
