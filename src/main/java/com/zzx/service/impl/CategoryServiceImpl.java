package com.zzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.common.CustomException;
import com.zzx.entity.Category;
import com.zzx.entity.Dish;
import com.zzx.entity.Setmeal;
import com.zzx.mapper.CategoryMapper;
import com.zzx.service.CategoryService;
import com.zzx.service.DishService;
import com.zzx.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
        @Autowired
        private DishService dishService;
        @Autowired
        private SetmealService setmealService;
    /**
     * 根据id删除分类,删除前需要判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        final LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询分类是否关联菜品
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int con1 = dishService.count(dishLambdaQueryWrapper);
        // 如果当前分类关联了菜品
        if(con1>0){
            // 已经关联菜品,抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除!");
        }
        // 查询分类是否关联套餐
        final LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int con2 = setmealService.count(setmealLambdaQueryWrapper);
        // 如果当前分类关联了套餐
        if(con2>0){
            // 已经关联套餐,抛出业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除!");
        }
        // 正常删除
        this.removeById(id);
    }
}
