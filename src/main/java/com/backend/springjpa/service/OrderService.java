package com.backend.springjpa.service;


import com.backend.springjpa.dto.*;
import com.backend.springjpa.entity.Cart;
import com.backend.springjpa.entity.CartItem;
import com.backend.springjpa.entity.Order;

import com.backend.springjpa.entity.OrderItem;
import com.backend.springjpa.entity.Payment;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.repository.CartItemRepository;
import com.backend.springjpa.repository.OrderRepository;
import com.backend.springjpa.repository.ProductVariantRepository;
import com.backend.springjpa.util.OrderStatus;
import com.backend.springjpa.util.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    public OrderService(OrderRepository orderRepository,
                        CartItemRepository cartItemRepository,
                        ProductVariantRepository productVariantRepository
                        ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional
    public void checkoutRaceCondition(CheckoutRequestDto dto) {

        Long cartId = Long.parseLong(dto.getCartId());

        Order order = Order.builder()
                .userId(Long.parseLong(dto.getUserId()))
                .status(OrderStatus.CREATED)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> cartItems = new ArrayList<>();

        for (CheckoutItemDto requestDto : dto.getItems()) {

            Long cartItemId = Long.parseLong(requestDto.getCartItemId());

            CartItem item = cartItemRepository
                    .findByIdAndCartId(cartItemId, cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
            cartItems.add(item);

            ProductVariant variant = item.getProductVariant();

            int updated = productVariantRepository
                    .decreaseStock(variant.getId(), item.getQuantity());

            if (updated == 0) {
                throw new BadRequestException(
                        "Stock not enough for variant " + variant.getName()
                );
            }

            BigDecimal itemTotal =
                    variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .qty(item.getQuantity())
                    .productVariant(variant) // OK untuk relasi
                    .price(variant.getPrice())
                    .priceAmount(itemTotal)
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            total = total.add(itemTotal);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);
        Payment payment = Payment.builder()
                .status(PaymentStatus.WAITING)
                .method(dto.getPaymentMethod().toUpperCase())
                .amount(total)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .order(order)
                .build();
        order.setPayment(payment);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

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
    }


    @Transactional
    public void cancelOrder(Order order) {
        order.getOrderItems().forEach(item ->
                item.getProductVariant().setStockQty(item.getProductVariant().getStockQty() + item.getQty()));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


    public List<DailySalesReportDto> getDailySalesReport(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getDailySalesReport(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
    }

    public List<SellerSalesReport> getSalesPerSeller(LocalDate start, LocalDate end) {
        return orderRepository.getSalesPerSeller(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public List<TopProductVariantReport> getTopSellingVariants(LocalDate start, LocalDate end) {
        return orderRepository.getTopSellingVariants(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public UserOrderSummaryReport getUserOrderSummary(Long userId, LocalDate start, LocalDate end) {
        return orderRepository.getUserOrderSummary(userId, start.atStartOfDay(),end.atTime(LocalTime.MAX));
    }

    public UserMostPurchasedProductReport getUserMostPurchasedProduct(Long userId, LocalDate start, LocalDate end) {
        return orderRepository.getUserMostPurchasedProduct(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public List<RevenuePerCategoryReport> getRevenuePerCategory(LocalDate start, LocalDate end) {
        return orderRepository.getRevenuePerCategory(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public List<DailyConversionRateView> getDailyConversionRate(LocalDate start, LocalDate end) {
        return orderRepository.getDailyConversionRate(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }
}
