package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.*;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        //fetch user
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));

        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("cart not found with given id"));

        List<CartItem> cartItems = cart.getItems();
        if(cartItems.size()<=0){
            throw new BadApiRequest("invalid number of items in cart!!");
        }

        Order order = Order.builder()
                        .billingName(orderDto.getBillingName())
                        .billingPhone(orderDto.getBillingPhone())
                        .billingAddress(orderDto.getBillingAddress())
                        .orderedDate(new Date())
                        .deliveredDate(null)
                        .paymentStatus(orderDto.getPaymentStatus())
                        .orderStatus(orderDto.getOrderStatus())
                        .orderId(UUID.randomUUID().toString())
                        .user(user)
                        .build();

        //orderitems and amount

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            //cartItem -> orderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity()* cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

       Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("order not found with given id"));
       orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));
        List<Order> orders = orderRepository.findByUser(user);

        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(CreateOrderRequest request, String orderId) {
        String orderStatus = request.getOrderStatus();
        String paymentStatus = request.getPaymentStatus();

         Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("order not found"));

         order.setOrderStatus(orderStatus);
         order.setPaymentStatus(paymentStatus);

         order.setOrderedDate(new Date());

         Order updatedOrder = orderRepository.save(order);
         return modelMapper.map(updatedOrder,OrderDto.class);
    }
}
