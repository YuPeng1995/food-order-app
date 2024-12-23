package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(("/admin/dish"))
@Api(tags = "Admin Dish Apis")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("Create new dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Create new dish: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // Clear Redis cache
        cleanRedisCache("dish_" + dishDTO.getCategoryId());
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

        // Clear Redis cache
        cleanRedisCache("dish_*");

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

        // Clear Redis cache
        cleanRedisCache("dish_*");


        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Enable or disable dish")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        dishService.enableOrDisable(status, id);

        // Clear Redis cache
        cleanRedisCache("dish_*");


        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("List dishes by category id")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    private void cleanRedisCache(String pattern) {
        log.info("Clean Redis cache by pattern: {}", pattern);
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
