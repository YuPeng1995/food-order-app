package com.sky.mapper;

import com.sky.entity.ComboMealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ComboMealDishMapper {

    List<Long> queryComboMealIdList(List<Long> dishIds);

    /**
     * 批量保存套餐和菜品的关联关系
     * @param comboMealDishes
     */
    void insertBatch(List<ComboMealDish> comboMealDishes);

    /**
     * 根据套餐id删除套餐和菜品的关联关系
     * @param comboMealId
     */
    @Delete("delete from combo_meal_dish where combo_meal_id = #{comboMealId}")
    void deleteByComboMealId(Long comboMealId);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param comboMealId
     * @return
     */
    @Select("select * from combo_meal_dish where combo_meal_id = #{comboMealId}")
    List<ComboMealDish> getByComboMealId(Long comboMealId);
}
