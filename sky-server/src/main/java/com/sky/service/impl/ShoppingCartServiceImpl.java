package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ComboMeal;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ComboMealMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ComboMealMapper comboMealMapper;

    @Autowired
    private DishMapper dishMapper;

    public void addOrReduceShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        // Check if the product is already in the shopping cart
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // If the product is already in the shopping cart, update the quantity
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            if (shoppingCartDTO.getIsAdd()) {
                cart.setQuantity(cart.getQuantity() + 1);
            } else {
                cart.setQuantity(cart.getQuantity() - 1);
            }
            shoppingCartMapper.updateQuantityById(cart);
        } else {
            // If the product is not in the shopping cart, add the product to the shopping cart

            // Check dish or combo meal
            if (shoppingCartDTO.getComboMealId() != 0) {
                ComboMeal comboMeal = comboMealMapper.getById(shoppingCartDTO.getComboMealId());
                shoppingCart.setName(comboMeal.getName());
                shoppingCart.setImage(comboMeal.getImage());
                shoppingCart.setPrice(comboMeal.getPrice());

            } else {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setPrice(dish.getPrice());

            }
            shoppingCart.setQuantity(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);

        }

    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void emptyShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.delete(userId);
    }
}
