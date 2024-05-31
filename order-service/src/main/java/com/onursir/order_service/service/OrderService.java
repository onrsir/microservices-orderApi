package com.onursir.order_service.service;


import com.onursir.order_service.dto.InventoryResponse;
import com.onursir.order_service.dto.OrderLineItemsDto;
import com.onursir.order_service.dto.OrderRequest;
import com.onursir.order_service.model.Order;
import com.onursir.order_service.model.OrderLineItems;
import com.onursir.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

       List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsListDto()
                .stream().map(this::convertToOrderLineItems)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

                // Call Inventory Service to check if the product is in stock

                InventoryResponse[] inventoryResponsArray = webClient.get()
                        .uri("http://localhost:8081/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

       boolean AllProductsInStock = Arrays.stream(inventoryResponsArray)
               .allMatch(InventoryResponse::isInStock);

        if (AllProductsInStock){
                    orderRepository.save(order);
                } else{
                    throw new RuntimeException("Product is not in stock");
                }

    }


    private OrderLineItems convertToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(orderLineItemsDto.getId());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());

        return orderLineItems;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
