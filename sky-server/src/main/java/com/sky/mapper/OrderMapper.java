package com.sky.mapper;

import com.sky.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Order order);

    @Select("select * from `order` where id = #{orderId}")
    Order getById(Long orderId);

    void update(Order order);

    @Select("select * from `order` where status = #{status} and order_time < #{orderTime}")
    List<Order> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);
}
