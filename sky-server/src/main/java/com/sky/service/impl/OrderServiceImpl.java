package com.sky.service.impl;

import com.paypal.base.rest.JSONFormatter;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.Order;
import com.sky.entity.OrderDetail;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // Possible service error handling (empty shopping cart, invalid address, etc.)
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_EMPTY);
        }

        // Insert order into order table
        Order order = new Order();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentStatus(Order.UNPAID);
        order.setStatus(Order.PAYMENT_PENDING);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhoneNumber(addressBook.getPhoneNumber());
        order.setRecipientName(addressBook.getName());
        order.setUserId(userId);

        orderMapper.insert(order);

        // Insert order details into order_detail table
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // Delete the shopping cart
        shoppingCartMapper.delete(userId);

        Map map = new HashMap();
        map.put("Type", 1);
        map.put("orderId", order.getId());
        map.put("content", "Order Number: " + order.getNumber());
        String json = JSONFormatter.toJSON(map);
        webSocketServer.sendToAllClient(json);



        // Return the order details
        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .orderNumber(order.getNumber())
                .orderPrices(order.getTotalPrices())
                .build();
    }

    @Override
    public Order getById(Long orderId) {
        return orderMapper.getById(orderId);
    }

    @Override
    public void updateOrderStatus(long orderId) {
        Order order = orderMapper.getById(orderId);
        order.setPaymentStatus(Order.PAID);
        order.setCheckoutTime(LocalDateTime.now());
        order.setStatus(Order.TO_BE_CONFIRMED);
        orderMapper.update(order);
    }
}
