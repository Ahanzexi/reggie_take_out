package com.zzx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzx.common.R;
import com.zzx.dto.DishDto;
import com.zzx.entity.Category;
import com.zzx.entity.Dish;
import com.zzx.entity.DishFlavor;
import com.zzx.service.CategoryService;
import com.zzx.service.DishFlavorService;
import com.zzx.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功!");
    }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        // 构造分页构造器
        final Page<Dish> pageInfo = new Page<>(page, pageSize);
        final Page<DishDto> dishDtoPage = new Page<>();
        final LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);
        // 排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        // 执行分页查询
        dishService.page(pageInfo,queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        // 基于dish生产dishDto的list
        List<DishDto> list = records.stream().map((item)->{
            // 构造对象
            final DishDto dishDto = new DishDto();
            // 获取分类名
            final Long categoryId = item.getCategoryId();
            final Category category = categoryService.getById(categoryId);
            String categoryName ="";
            if (category!=null) {
                categoryName = category.getName();
            }
            // 为对象赋值
            BeanUtils.copyProperties(item,dishDto);
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        // 设置
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 编辑回显,根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        final DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    /*D 404*/

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("修改菜品 {}",dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功!");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids){
        log.info("删除菜品 {}",ids);
        for (Long id:ids) {
            dishService.deleteWithFlavor(id);
        }
        return R.success("删除菜品成功");
    }

    /**
     * 批量修改状态方法
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(Long[] ids, @PathVariable Integer status){
        final Dish dish = new Dish();
        for (Long id:ids) {
            dish.setStatus(status);
            dish.setId(id);
            dishService.updateById(dish);
        }
        return R.success(status==0?"已禁售":"已启售");
    }

}
