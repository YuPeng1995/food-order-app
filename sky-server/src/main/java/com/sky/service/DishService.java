package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    public void saveWithFlavor(DishDTO dishDTO);

    PageResult queryByPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO queryDishWithFlavorById(Long id);

    void updateDishWithFlavor(DishDTO dishDTO);

    List<DishVO> listWithFlavor(Dish dish);

    List<Dish> list(Long categoryId);

    void enableOrDisable(Integer status, Long id);
}
