package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.ComboMealDTO;
import com.sky.dto.ComboMealPageQueryDTO;
import com.sky.entity.ComboMeal;
import com.sky.entity.ComboMealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.ComboMealDishMapper;
import com.sky.mapper.ComboMealMapper;
import com.sky.result.PageResult;
import com.sky.service.ComboMealService;
import com.sky.vo.ComboMealVO;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ComboMealServiceImpl implements ComboMealService {

    @Autowired
    private ComboMealMapper comboMealMapper;

    @Autowired
    private ComboMealDishMapper comboMealDishMapper;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param comboMealDTO
     */
    @Transactional
    public void saveWithDish(ComboMealDTO comboMealDTO) {
        ComboMeal comboMeal = new ComboMeal();
        BeanUtils.copyProperties(comboMealDTO, comboMeal);

        //向套餐表插入数据
        comboMealMapper.insert(comboMeal);

        //获取生成的套餐id
        Long comboMealId = comboMeal.getId();

        List<ComboMealDish> comboMealDishes = comboMealDTO.getComboMealDishes();
        comboMealDishes.forEach(comboMealDish -> {
            comboMealDish.setComboMealId(comboMealId);
        });

        //保存套餐和菜品的关联关系
        comboMealDishMapper.insertBatch(comboMealDishes);
    }

    /**
     * 分页查询
     *
     * @param comboMealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(ComboMealPageQueryDTO comboMealPageQueryDTO) {
        int pageNum = comboMealPageQueryDTO.getPage();
        int pageSize = comboMealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<ComboMealVO> page = comboMealMapper.pageQuery(comboMealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            ComboMeal comboMeal = comboMealMapper.getById(id);
            if (StatusConstant.ENABLE == comboMeal.getStatus()) {
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.COMBO_MEAL_ON_SALE);
            }
        });

        ids.forEach(comboMealId -> {
            //删除套餐表中的数据
            comboMealMapper.deleteById(comboMealId);
            //删除套餐菜品关系表中的数据
            comboMealDishMapper.deleteByComboMealId(comboMealId);
        });
    }

    /**
     * 根据id查询套餐和套餐菜品关系
     *
     * @param id
     * @return
     */
    public ComboMealVO getByIdWithDish(Long id) {
        ComboMeal comboMeal = comboMealMapper.getById(id);
        List<ComboMealDish> comboMealDishes = comboMealDishMapper.getByComboMealId(id);

        ComboMealVO comboMealVO = new ComboMealVO();
        BeanUtils.copyProperties(comboMeal, comboMealVO);
        comboMealVO.setComboMealDishes(comboMealDishes);

        return comboMealVO;
    }

    /**
     * 修改套餐
     *
     * @param comboMealDTO
     */
    @Transactional
    public void update(ComboMealDTO comboMealDTO) {
        ComboMeal comboMeal = new ComboMeal();
        BeanUtils.copyProperties(comboMealDTO, comboMeal);

        //1、修改套餐表，执行update
        comboMealMapper.update(comboMeal);

        //套餐id
        Long comboMealId = comboMealDTO.getId();

        //2、删除套餐和菜品的关联关系，操作comboMeal_dish表，执行delete
        comboMealDishMapper.deleteByComboMealId(comboMealId);

        List<ComboMealDish> comboMealDishes = comboMealDTO.getComboMealDishes();
        comboMealDishes.forEach(comboMealDish -> {
            comboMealDish.setComboMealId(comboMealId);
        });
        //3、重新插入套餐和菜品的关联关系，操作comboMeal_dish表，执行insert
        comboMealDishMapper.insertBatch(comboMealDishes);
    }

    /**
     * 条件查询
     * @param comboMeal
     * @return
     */
    public List<ComboMeal> list(ComboMeal comboMeal) {
        List<ComboMeal> list = comboMealMapper.list(comboMeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return comboMealMapper.getDishItemByComboMealId(id);
    }

    @Override
    public void enableAndDisable(Integer status, Long id) {
        ComboMeal comboMeal = comboMealMapper.getById(id);
        comboMeal.setStatus(status);
        comboMealMapper.update(comboMeal);
    }

}
