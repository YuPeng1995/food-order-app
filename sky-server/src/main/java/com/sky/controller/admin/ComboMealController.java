package com.sky.controller.admin;

import com.sky.dto.ComboMealDTO;
import com.sky.dto.ComboMealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ComboMealService;
import com.sky.vo.ComboMealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result update(@RequestBody ComboMealDTO comboMealDTO) {
        comboMealService.update(comboMealDTO);
        return Result.success();
    }
}
