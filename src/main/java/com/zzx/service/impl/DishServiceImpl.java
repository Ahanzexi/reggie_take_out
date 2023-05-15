package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.Category;
import com.zzx.entity.Dish;
import com.zzx.mapper.CategoryMapper;
import com.zzx.mapper.DishMapper;
import com.zzx.service.CategoryService;
import com.zzx.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
