package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.ComboMeal;
import com.sky.result.Result;
import com.sky.service.ComboMealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userComboMealController")
@RequestMapping("/user/combo_meal")
@Api(tags = "User Combo Meal Controller")
public class ComboMealController {

    @Autowired
    private ComboMealService comboMealService;

    /**
     * 条件查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query Combo Meal by Category ID")
    public Result<List<ComboMeal>> list(Long categoryId) {
        ComboMeal comboMeal = new ComboMeal();
        comboMeal.setCategoryId(categoryId);
        comboMeal.setStatus(StatusConstant.ENABLE);

        List<ComboMeal> list = comboMealService.list(comboMeal);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("Query Dish by Combo Meal ID")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = comboMealService.getDishItemById(id);
        return Result.success(list);
    }

}
