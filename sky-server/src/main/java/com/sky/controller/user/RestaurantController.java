package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userRestaurantController")
@RequestMapping("/user/restaurant")
@Slf4j
@Api(tags = "User restaurant Apis")
public class RestaurantController {

    public static final String RESTAURANT_STATUS = "restaurant_status";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("Get restaurant status for user")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(RESTAURANT_STATUS);
        log.info("Get restaurant status: {}", status == 1 ? "open" : "close");
        return Result.success(status);
    }
}


