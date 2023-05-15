package com.zzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzx.entity.Category;
import com.zzx.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
