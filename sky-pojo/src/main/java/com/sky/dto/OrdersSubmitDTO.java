package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {
    //地址簿id
    private Long addressBookId;
    //付款方式
    private int paymentMethod;
    //备注
    private String orderNote;
    //预计送达时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;
    //配送状态  1立即送出  0选择具体时间
    private Integer deliveryStatus;
    //餐具数量
    private Integer utensilsNumber;
    //餐具数量状态  1按餐量提供  0选择具体数量
    private Integer utensilsStatus;
    //打包费
    private Integer packFees;
    //总金额
    private BigDecimal totalPrices;
}
