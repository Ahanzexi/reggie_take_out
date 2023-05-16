package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.Setmeal;
import com.zzx.entity.SetmealDish;
import com.zzx.mapper.SetmealDishMapper;
import com.zzx.mapper.SetmealMapper;
import com.zzx.service.SetmealDishService;
import com.zzx.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
