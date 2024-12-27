package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart set quantity = #{quantity} where id = #{id}")
    void updateQuantityById(ShoppingCart cart);

    @Insert("insert into shopping_cart (name, user_id, dish_id, combo_meal_id, dish_flavor, quantity, price, image, create_time) " +
            "values (#{name}, #{userId}, #{dishId}, #{comboMealId}, #{dishFlavor}, #{quantity}, #{price}, #{image}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void delete(Long userId);
}
