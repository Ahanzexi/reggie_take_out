package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.Dish;
import com.zzx.entity.Setmeal;
import com.zzx.mapper.DishMapper;
import com.zzx.mapper.SetmealMapper;
import com.zzx.service.DishService;
import com.zzx.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
