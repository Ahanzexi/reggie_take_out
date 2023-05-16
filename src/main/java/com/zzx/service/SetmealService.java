package com.zzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzx.dto.SetmealDto;
import com.zzx.entity.Dish;
import com.zzx.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐同时保存关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

}
