package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.ComboMealPageQueryDTO;
import com.sky.entity.ComboMeal;
import com.sky.entity.ComboMealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.ComboMealVO;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ComboMealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from combo_meal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param comboMeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(ComboMeal comboMeal);

    /**
     * 分页查询
     * @param comboMealPageQueryDTO
     * @return
     */
    Page<ComboMealVO> pageQuery(ComboMealPageQueryDTO comboMealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from combo_meal where id = #{id}")
    ComboMeal getById(Long id);

    /**
     * 根据id删除套餐
     * @param comboMealId
     */
    @Delete("delete from combo_meal where id = #{id}")
    void deleteById(Long comboMealId);

    /**
     * 修改套餐
     * @param comboMeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(ComboMeal comboMeal);

    List<ComboMeal> list(ComboMeal comboMeal);

    @Select("select cd.name, cd.copies, d.image, d.description " +
            "from combo_meal_dish cd left join dish d on cd.dish_id = d.id " +
            "where cd.combo_meal_id = #{comboMealId}")
    List<DishItemVO> getDishItemByComboMealId(Long id);
}
