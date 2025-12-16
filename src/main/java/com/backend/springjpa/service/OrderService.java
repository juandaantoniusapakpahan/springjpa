package com.backend.springjpa.service;


import com.backend.springjpa.dto.CheckoutRequestDto;
import com.backend.springjpa.entity.CartItem;
import com.backend.springjpa.entity.Order;

import com.backend.springjpa.entity.OrderItem;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.repository.CartItemRepository;
import com.backend.springjpa.repository.OrderRepository;
import com.backend.springjpa.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartItemRepository cartItemRepository;
    public OrderService(OrderRepository orderRepository,
                        CartItemRepository cartItemRepository
                        ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public void checkOut(CheckoutRequestDto requestDto) {

        Long cartId = Long.parseLong(requestDto.getCartId());

        Order order = Order.builder()
                .userId(Long.parseLong(requestDto.getUserId()))
                .status(OrderStatus.CREATED)
                .build();

        List<Long> cartItemIds = requestDto.getItems().stream()
                .map(dto -> Long.parseLong(dto.getCartItemId()))
                .toList();

        List<CartItem> cartItems =
                cartItemRepository.findAllByIdInAndCartId(cartItemIds, cartId);

        if (cartItems.size() != cartItemIds.size()) {
            throw new ResourceNotFoundException("Some cart items not found");
        }

        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();

            if (cartItem.getQuantity() > variant.getStockQty()) {
                throw new BadRequestException(
                        variant.getName() + " not enough stock"
                );
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            ProductVariant variant = cartItem.getProductVariant();

            variant.setStockQty(
                    variant.getStockQty() - cartItem.getQuantity()
            );

            BigDecimal itemTotal =
                    variant.getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .productVariant(variant)
                    .qty(cartItem.getQuantity())
                    .priceAmount(itemTotal)
                    .price(variant.getPrice())
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            total = total.add(itemTotal);
        }
        order.setOrderItems(orderItems);


        order.setTotalAmount(total);

        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        //return order;
    }

}
