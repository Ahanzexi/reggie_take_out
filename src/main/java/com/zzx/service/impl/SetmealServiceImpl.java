package com.zzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.common.CustomException;
import com.zzx.dto.DishDto;
import com.zzx.dto.SetmealDto;
import com.zzx.entity.Dish;
import com.zzx.entity.Setmeal;
import com.zzx.entity.SetmealDish;
import com.zzx.mapper.DishMapper;
import com.zzx.mapper.SetmealMapper;
import com.zzx.service.DishService;
import com.zzx.service.SetmealDishService;
import com.zzx.service.SetmealService;
import org.springframework.beans.BeanUtils;
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

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        // 查询是否可以删除,即套餐的status
        final LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        final int count = this.count(queryWrapper);
        if(count > 0){
            // 不能删除还含有在售菜品
            throw new CustomException("删除失败,该套餐任在售!");
        }
        this.removeByIds(ids);
        // 删除关系表中的数据
        final LambdaQueryWrapper<SetmealDish> q = new LambdaQueryWrapper<>();
        q.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(q);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        // 查询基本信息
        Setmeal setmeal = this.getById(id);
        // 查询菜品信息
        final LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getId()!=null,SetmealDish::getSetmealId,setmeal.getId());
        final List<SetmealDish> list = setmealDishService.list(queryWrapper);
        // 构造新的对象
        final SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //更新基本信息
        this.updateById(setmealDto);

        //清理之前的菜品
        final LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDto.getId()!=null,SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //添加当前的菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        setmealDishes = setmealDishes.stream().peek((item)-> item.setSetmealId(id)).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
