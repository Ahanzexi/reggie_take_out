package com.zzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzx.entity.ShoppingCart;
import com.zzx.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
