package com.sky.service;

import com.sky.dto.ComboMealDTO;
import com.sky.dto.ComboMealPageQueryDTO;
import com.sky.entity.ComboMeal;
import com.sky.result.PageResult;
import com.sky.vo.ComboMealVO;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface ComboMealService {

    void saveWithDish(ComboMealDTO comboMealDTO);

    /**
     * 分页查询
     * @param comboMealPageQueryDTO
     * @return
     */
    PageResult pageQuery(ComboMealPageQueryDTO comboMealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
     */
    ComboMealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param comboMealDTO
     */
    void update(ComboMealDTO comboMealDTO);

    List<ComboMeal> list(ComboMeal comboMeal);

    List<DishItemVO> getDishItemById(Long id);

    void enableAndDisable(Integer status, Long id);
}
