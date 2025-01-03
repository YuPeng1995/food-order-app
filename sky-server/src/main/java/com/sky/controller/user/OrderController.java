package com.sky.controller.user;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Order;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.PayPalService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController("UserOrderController")
@RequestMapping("/user/order")
@Api(tags = "User Order Apis")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayPalService payPalService;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @Value("${paypal.success.url}")
    private String successUrl;

    @PostMapping("/submit")
    @ApiOperation("User submit order")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("User submit order: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PostMapping("/pay")
    @ApiOperation("Process PayPal payment")
    public Result<String> payment(@RequestParam("orderId") Long orderId) {
        try {
            // Get order details
            Order order = orderService.getById(orderId);

            Payment payment = payPalService.createPayment(
                    order.getTotalPrices().doubleValue(),
                    "USD",
                    "paypal",
                    "sale",
                    "Order #" + orderId,
                    cancelUrl,
                    successUrl
            );

            for(Links links : payment.getLinks()) {
                if(links.getRel().equals("approval_url")) {
                    return Result.success(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal payment error", e);
            return Result.error(MessageConstant.PAYMENT_FAILED);
        }
        return Result.error(MessageConstant.PAYMENT_FAILED);
    }

    @GetMapping("/pay/success")
    @ApiOperation("Handle successful PayPal payment")
    public Result<String> successPay(@RequestParam("paymentId") String paymentId,
                                     @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")) {
                // Update order status
                String orderId = payment.getTransactions().get(0).getDescription().replace("Order #", "");
                log.info("Order {} payment successful", orderId);
                orderService.updateOrderStatus(Long.parseLong(orderId));
                return Result.success(MessageConstant.PAYMENT_SUCCESS);
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal payment execution error", e);
            return Result.error("Payment execution failed");
        }
        return Result.error("Payment execution failed");
    }

    @GetMapping("/pay/cancel")
    @ApiOperation("Handle cancelled PayPal payment")
    public Result<String> cancelPay() {
        return Result.success("Payment cancelled");
    }

}
