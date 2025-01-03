package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Order;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    Order getById(Long orderId);

    void updateOrderStatus(long orderId);
}
