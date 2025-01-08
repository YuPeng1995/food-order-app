package com.sky.controller.user;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "User Category Controller")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query Category")
    public Result<List<Category>> list(Integer type) {
        log.info("Query Category: type={}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }

    @GetMapping("/page")
    @ApiOperation("Query categories by page")
    public Result<PageResult> queryByPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.queryByPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

}
