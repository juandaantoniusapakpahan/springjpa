package com.backend.springjpa.service;

import com.backend.springjpa.entity.OrderItem;
import com.backend.springjpa.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getOrderItemByOrderId(Long orderId) {
       return orderItemRepository.findByOrderId(orderId);
    }
}
