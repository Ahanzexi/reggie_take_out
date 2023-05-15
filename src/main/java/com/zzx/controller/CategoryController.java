package com.zzx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzx.common.R;
import com.zzx.entity.Category;
import com.zzx.entity.Employee;
import com.zzx.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息: {}",category.toString());
        categoryService.updateById(category);
        return R.success("修改分类信息成功!");
    }

    /**
     * 通过id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R<String> delete(Long ids){
        log.info("删除分类: {}",ids);
        //categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除分类成功!");
    }


    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping()
    public R<String> save(@RequestBody Category category){
        log.info("新增分类: {}",category);
        categoryService.save(category);
        return R.success("新增分类成功!");
    }

    /**
     * 分页查询分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize){
        log.info("page {},pageSize {}",page,pageSize);
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}
