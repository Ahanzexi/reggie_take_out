package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.dto.SetmealDto;
import com.zzx.entity.Dish;
import com.zzx.entity.Setmeal;
import com.zzx.entity.SetmealDish;
import com.zzx.mapper.DishMapper;
import com.zzx.mapper.SetmealMapper;
import com.zzx.service.DishService;
import com.zzx.service.SetmealDishService;
import com.zzx.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存基本信息
        this.save(setmealDto);

        // 保存套餐和菜品的关联信息
        final List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().peek((item)-> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
