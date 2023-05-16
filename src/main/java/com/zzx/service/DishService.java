package com.zzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzx.dto.DishDto;
import com.zzx.entity.Category;
import com.zzx.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    // 新增菜品,并加入口味数据
     void saveWithFlavor(DishDto dishDto);

      DishDto getByIdWithFlavor(Long id);

      void updateWithFlavor(DishDto dishDto);

    void deleteWithFlavor(List<Long> ids);
}
