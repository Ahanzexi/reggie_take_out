package com.zzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.dto.DishDto;
import com.zzx.entity.Category;
import com.zzx.entity.Dish;
import com.zzx.entity.DishFlavor;
import com.zzx.mapper.CategoryMapper;
import com.zzx.mapper.DishMapper;
import com.zzx.service.CategoryService;
import com.zzx.service.DishFlavorService;
import com.zzx.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存基本信息
        this.save(dishDto);
        final Long dishId = dishDto.getId();
        // 菜品口味
        final List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().peek((item)-> item.setDishId(dishId)).collect(Collectors.toList());
        // 保存口味
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        // 查询基本信息
        Dish dish = this.getById(id);

        // 查询口味信息
        // 条件构造器
        final LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        final List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        final DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新基本操作
        this.updateById(dishDto);

        // 清理之前的菜品口味
        final LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // 添加当前提交的菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishDtoId = dishDto.getId();
        flavors = flavors.stream().peek((item)-> item.setDishId(dishDtoId)).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void deleteWithFlavor(Long id) {
        // 删除基本信息
        this.removeById(id);
        // 删除口味信息
        final LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(queryWrapper);
    }
}
