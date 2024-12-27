package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "User shopping cart Apis")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add_or_reduce")
    @ApiOperation("Add or reduce shopping cart")
    public Result addOrReduce(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("Add or reduce shopping cart: {}", shoppingCartDTO);
        shoppingCartService.addOrReduceShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("List shopping cart")
    public Result<List<ShoppingCart>> list() {
        log.info("List shopping cart");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    @DeleteMapping("/empty_cart")
    @ApiOperation("Delete shopping cart")
    public Result empty() {
        log.info("Empty shopping cart");
        shoppingCartService.emptyShoppingCart();
        return Result.success();
    }
}
