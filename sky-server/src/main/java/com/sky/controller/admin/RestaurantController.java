package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminRestaurantController")
@RequestMapping("/admin/restaurant")
@Api(tags = "Admin restaurant Apis")
@Slf4j
public class RestaurantController {

    public static final String RESTAURANT_STATUS = "restaurant_status";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("Set restaurant status")
    public Result setStatus(@PathVariable Integer status) {
        log.info("Set restaurant status: {}", status == 1 ? "open" : "close");
        redisTemplate.opsForValue().set(RESTAURANT_STATUS, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("Get restaurant status for admin")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(RESTAURANT_STATUS);
        log.info("Get restaurant status: {}", status == 1 ? "open" : "close");
        return Result.success(status);
    }

}
