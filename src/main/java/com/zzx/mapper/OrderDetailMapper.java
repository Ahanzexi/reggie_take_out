package com.zzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzx.entity.OrderDetail;
import com.zzx.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
