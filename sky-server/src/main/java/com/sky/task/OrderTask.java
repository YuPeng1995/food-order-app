package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Order;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOutOrder() {
        log.info("Process TimeOut Order: {}", LocalDateTime.now());

        // Cancel orders that have been in the pending payment state for more than 15 minutes
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Order> orders = orderMapper.getByStatusAndOrderTime(Order.PAYMENT_PENDING, time);

        if (orders != null && orders.size() > 0) {
            for (Order order : orders) {
                order.setStatus(Order.CANCELLED);
                order.setCancelReason(MessageConstant.ORDER_CANCELLED_TIMEOUT);
                order.setCheckoutTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("Process Delivery Order: {}", LocalDateTime.now());

        // Update the status of orders that have been paid to the delivery state
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        List<Order> orders = orderMapper.getByStatusAndOrderTime(Order.DELIVERY_IN_PROGRESS, time);

        if (orders != null && orders.size() > 0) {
            for (Order order : orders) {
                order.setStatus(Order.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

}
