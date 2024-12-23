package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "User Dish Controller")
public class DishController {

    @Autowired
    private DishService dishService;
    
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query Dish by Category ID")
    public Result<List<DishVO>> list(Long categoryId) {
        
        // Create a key for Redis
        String key = "dish_" + categoryId;

        // Query dish from Redis if exists
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0) {
            // If dish exists in Redis, return it, no need to query from database
            log.info("Get dish list from Redis");
            return Result.success(list);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        // If dish does not exist in Redis, query from database, then save it to Redis
        list = dishService.listWithFlavor(dish);
        log.info("Save dish list to Redis");
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
