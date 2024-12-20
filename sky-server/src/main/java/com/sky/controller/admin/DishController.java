package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/admin/dish"))
@Api(tags = "Dish Apis")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("Create new dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Create new dish: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Query dishes by page")
    public Result<PageResult> queryByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("Query dishes by page: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.queryByPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Delete dishes")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("Delete dishes: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get dish info and flavor info by id")
    public Result<DishVO> queryDishWithFlavorById(@PathVariable Long id) {
        log.info("Get dish with flavor info of: {}", id);
        DishVO dishVO = dishService.queryDishWithFlavorById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("Update dish info and flavor info by id")
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.updateDishWithFlavor(dishDTO);
        return Result.success();
    }

}
