package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.Dish;
import com.zzx.entity.DishFlavor;
import com.zzx.mapper.DishFlavorMapper;
import com.zzx.mapper.DishMapper;
import com.zzx.service.DishFlavorService;
import com.zzx.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
