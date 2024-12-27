package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;
    private Long comboMealId;
    private String dishFlavor;
    private Boolean isAdd;

}
