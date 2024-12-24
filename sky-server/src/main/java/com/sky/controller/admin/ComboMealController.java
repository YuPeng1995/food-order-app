package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.ComboMealDTO;
import com.sky.dto.ComboMealPageQueryDTO;
import com.sky.entity.ComboMeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ComboMealService;
import com.sky.vo.ComboMealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/combo_meal")
@Api(tags = "Admin Combo Meal Apis")
@Slf4j
public class ComboMealController {

    @Autowired
    private ComboMealService comboMealService;

    /**
     * 新增套餐
     * @param comboMealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Create new combo meal")
    @CacheEvict(cacheNames = "comboMealCache", key = "#comboMealDTO.categoryId")
    public Result save(@RequestBody ComboMealDTO comboMealDTO) {
        comboMealService.saveWithDish(comboMealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param comboMealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Query combo meal by page")
    public Result<PageResult> page(ComboMealPageQueryDTO comboMealPageQueryDTO) {
        PageResult pageResult = comboMealService.pageQuery(comboMealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete batch combo meals")
    @CacheEvict(cacheNames = "comboMealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        comboMealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐，用于修改页面回显数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get combo meal by id")
    public Result<ComboMealVO> getById(@PathVariable Long id) {
        ComboMealVO comboMealVO = comboMealService.getByIdWithDish(id);
        return Result.success(comboMealVO);
    }

    /**
     * 修改套餐
     *
     * @param comboMealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update combo meal")
    @CacheEvict(cacheNames = "comboMealCache", allEntries = true)
    public Result update(@RequestBody ComboMealDTO comboMealDTO) {
        comboMealService.update(comboMealDTO);
        return Result.success();
    }

    /**
     * 启用或禁用套餐
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Enable or disable combo meal")
    @CacheEvict(cacheNames = "comboMealCache", allEntries = true)
    public Result enableAndDisable(@PathVariable Integer status, Long id) {
        comboMealService.enableAndDisable(status, id);
        return Result.success();
    }
}
