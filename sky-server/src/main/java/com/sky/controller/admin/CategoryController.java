package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Admin Category Apis")
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("Query category by type")
    public Result<List<Category>> list(Integer type) {
        List<Category> categories = categoryService.list(type);
        return Result.success(categories);
    }

    @PutMapping
    @ApiOperation("Update category")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Query categories by page")
    public Result<PageResult> queryByPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.queryByPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Enable or disable category")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        categoryService.enableOrDisable(status, id);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("Create new category")
    public Result create(@RequestBody CategoryDTO categoryDTO) {
        categoryService.create(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("Delete category")
    public Result deleteById(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }
}
